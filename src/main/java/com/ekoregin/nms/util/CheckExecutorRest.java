package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import com.google.gson.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        if (check.getCheckScope().equals(CheckScope.CUSTOMER.name())) {
            log.info("Starting check execute with REST API for customer");
            checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
            paramsForCheck = getParamsForCheck(check, customer);
        } else if (check.getCheckScope().equals(CheckScope.DEVICE.name())) {
            log.info("Starting check execute with REST API for device");
            checkDevice = device;
        }
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        String requestWithValues = replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck);
        List<String> filter = Collections.emptyList();
        if (check.getJsonFilter() != null && !check.getJsonFilter().isEmpty()) {
            filter = List.of(check.getJsonFilter().split(";"));
            log.info("Filter set: " + filter);
        }
        return restExec(checkDevice, requestWithValues, filter);
    }

    private CheckResult restExec(Device device, String request, List<String> filter) {
        StringBuilder fullURI = new StringBuilder("http://").append(device.getIp().getAddress()).append(request);
//        StringBuilder fullURI = new StringBuilder("https://").append("jsonplaceholder.typicode.com").append(request);
        log.info("FullURL: {}", fullURI);
        CheckResult checkResult = new CheckResult();
        WebClient webClient = WebClient.builder()
                .defaultHeaders(header -> header.setBasicAuth(device.getLogin(), device.getPassword()))
                .build();
        String result = webClient
                .get()
                .uri(fullURI.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        checkResult.setResult(prettyPrintUsingGson(result, filter).replaceAll("[{}]", "").trim());
        return checkResult;
    }

    public String prettyPrintUsingGson(String uglyJson, List<String> allowKeys) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(uglyJson);

        if (jsonElement.isJsonArray())
            jsonElement = jsonElement.getAsJsonArray().get(0);

        String resultJson = gson.toJson(jsonElement);
        log.info("Result Json: " + resultJson);

        JsonElement filteredJsonElement = new JsonObject();
        log.info("Цикл по элементам JSON");
        log.info("ALlow keys: " + allowKeys.size());
        if (allowKeys.size() > 0) {
            for (String key : allowKeys) {
                log.info(jsonElement.getAsJsonObject().get(key).toString());
                if (!jsonElement.getAsJsonObject().get(key).isJsonNull()) {
                    filteredJsonElement.getAsJsonObject().add(key, jsonElement.getAsJsonObject().get(key));
                }
            }
            resultJson = gson.toJson(filteredJsonElement);
        }

        return resultJson;
    }
}
