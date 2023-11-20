package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import com.google.gson.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorRest implements CheckExecutor {

    private final ModelDeviceService modelDeviceService;

    @Override
    public CheckResult checkExecute(Check check, Customer customer, Device device) {
        Device checkDevice = null;
        Map<String, String> paramsForCheck = new HashMap<>();
        String checkScope = check.getCheckScope();
        if (checkScope.equals(CheckScope.CUSTOMER.name())) {
            log.info("Starting check execute with REST API for customer");
            checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
            paramsForCheck = getParamsForCheck(check, customer);
        } else if (checkScope.equals(CheckScope.DEVICE.name())) {
            log.info("Starting check execute with REST API for device");
            checkDevice = device;
        }
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        String requestWithValues = replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck, customer);
        List<String> filter = Collections.emptyList();
        if (check.getJsonFilter() != null && !check.getJsonFilter().isEmpty()) {
            filter = List.of(check.getJsonFilter().split(";"));
            log.info("Filter set: " + filter);
        }
        CheckResult checkResult = new CheckResult();
        String resultJson = restExec(checkDevice, requestWithValues, filter);
        if (checkScope.equals(CheckScope.CUSTOMER.name())) {
            resultJson = resultJson.replaceAll("[{}]", "").replaceAll("[\\[\\]]", "").trim();
        }
        checkResult.setResult(resultJson);
        return checkResult;
    }

    private String restExec(Device device, String request, List<String> filter) {
        StringBuilder fullURI = new StringBuilder("http://").append(device.getIp().getAddress()).append(request);
//        StringBuilder fullURI = new StringBuilder("https://").append("jsonplaceholder.typicode.com").append(request);
        log.info("FullURL: {}", fullURI);

        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                .build();
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .defaultHeaders(header -> header.setBasicAuth(device.getLogin(), device.getPassword()))
                .build();
        String result = webClient
                .get()
                .uri(fullURI.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return prettyPrintUsingGson(result, filter);
    }

    public String prettyPrintUsingGson(String uglyJson, List<String> allowKeys) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(uglyJson);

        String resultJson = gson.toJson(jsonElement);

        if (jsonElement.isJsonArray()) {
            log.info("JSON is ARRAY!");
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (!allowKeys.isEmpty()) {
                log.info("ALlow keys: " + allowKeys.size());
                jsonArray = new JsonArray();
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    jsonArray.add(filterJson(allowKeys, element));
                }
            }
            resultJson = gson.toJson(jsonArray);
        } else {
            if (!allowKeys.isEmpty()) {
                filterJson(allowKeys, jsonElement);
                resultJson = gson.toJson(jsonElement);
            }
        }
        log.info("Result Json: " + resultJson);
        return resultJson;
    }

    private JsonElement filterJson(List<String> allowKeys, JsonElement jsonElement) {
        JsonElement filteredJsonElement = new JsonObject();
        log.info("Фильтрация элементов в JSON");
        for (String key : allowKeys) {
            if (jsonElement != null && !jsonElement.getAsJsonObject().isJsonNull()) {
                filteredJsonElement.getAsJsonObject().add(key, jsonElement.getAsJsonObject().get(key));
            }
        }
        return filteredJsonElement;
    }
}
