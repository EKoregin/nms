package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.service.ModelDeviceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorMikrotik implements CheckExecutor {

    private final ModelDeviceService modelDeviceService;

    @Override
    public CheckResult checkExecute(Check check, Customer customer) {
        log.info("Starting check execute with MIKROTIK API");
        Device checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");
        Map<String, String> paramsForCheck = getParamsForCheck(check, customer);
        String requestWithValues = replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck);
        return restExec(checkDevice, requestWithValues);
    }

    private CheckResult restExec(Device device, String request) {
        StringBuilder fullURI = new StringBuilder("http://").append(device.getIp().getAddress()).append(request);
//        StringBuilder fullURI = new StringBuilder("https://").append("openlibrary.org").append(request);
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

        checkResult.setResult(prettyPrintUsingGson(result).replaceAll("[{}]", "").trim());
        return checkResult;
    }

    public String prettyPrintUsingGson(String uglyJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(uglyJson);
        return gson.toJson(jsonElement);
    }
}
