package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.Customer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GatherDataService {

    private final CustomerService customerService;
    private final CheckService checkService;

    public void getMacByIP(long deviceId, long checkId) {
        List<Customer> customers = customerService.findAll();
        String resultString = checkService.executeForDevice(checkId, deviceId).getResult();
        // Get JSON from String
        JsonElement jsonElement = JsonParser.parseString(resultString);
        // Loop for elements
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            log.info(element.getAsJsonObject().toString());
        }

    }
}
