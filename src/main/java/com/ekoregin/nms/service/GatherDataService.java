package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.entity.TypeTechParameter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GatherDataService {

    private final CustomerService customerService;
    private final CheckService checkService;

//    public void getMacByIP(long deviceId, long checkId) {
//        List<Customer> customers = customerService.findAll();
//        String resultString = checkService.executeForDevice(checkId, deviceId).getResult();
//        // Get JSON from String
//        JsonElement jsonElement = JsonParser.parseString(resultString);
//        // Loop for elements
//        for (JsonElement element : jsonElement.getAsJsonArray()) {
//            log.info(element.getAsJsonObject().toString());
//            if (!element.getAsJsonObject().isJsonNull()) {
//                String status = String.valueOf(element.getAsJsonObject().get("name"));
//                String activeIpAddress = String.valueOf(element.getAsJsonObject().get("username"));
//                String activeMacAddress = String.valueOf(element.getAsJsonObject().get("email"));
//                log.info("IP: {}, MAC: {}, STATUS: {}", activeIpAddress, activeMacAddress, status);
//            }
//        }
//    }

    public List<String> getMacByIP(long deviceId, long checkId) {
        List<Customer> customers = customerService.findAll();
        String resultString = checkService.executeForDevice(checkId, deviceId).getResult();
        // Get JSON from String
        JsonElement jsonElement = JsonParser.parseString(resultString);
        // Loop for elements
        Map<String, String> mapIpMac = new HashMap<>();
        List<String> list = new ArrayList<>();

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            if (!element.getAsJsonObject().isJsonNull()) {
                log.info(element.getAsJsonObject().toString());
                String status = String.valueOf(element.getAsJsonObject().get("status")).replaceAll("\"", "");
                String activeIpAddress = String.valueOf(element.getAsJsonObject().get("active-address")).replaceAll("\"", "");
                String activeMacAddress = String.valueOf(element.getAsJsonObject().get("active-mac-address")).replaceAll("\"", "");
//                log.info("IP: {}, MAC: {}, STATUS: {}", activeIpAddress, activeMacAddress, status);
                String getRecord = "IP: " +
                        activeIpAddress + ", MAC: " +
                        activeMacAddress + ", Status: " +
                        status;
                if (status.equals("bound")) {
                    mapIpMac.put(activeIpAddress, activeMacAddress);
                }
                list.add(getRecord);
            }
        }


        //loop for customers
        for (Customer customer : customers) {
            String name = customer.getName();
            //get ip address
            String ipAddress = customer.getParams().stream()
                    .filter(param -> param.getType().getName().equals("IP"))
                    .findFirst().orElse(new TechParameter(null, null, "Не определен", null)).getValue();
            //get mac address
            String macAddress = customer.getParams().stream()
                    .filter(param -> param.getType().getName().equals("MAC"))
                    .findFirst().orElse(new TechParameter(null, null, "Не определен", null)).getValue();

            String newMacAddress = mapIpMac.getOrDefault(ipAddress, "Не найден");

//            log.info("Name: {}, IP: {}, MAC: {}, New MAC: {}", name, ipAddress, macAddress, newMacAddress);
            String currentRecord = name + ", IP: " +
                    ipAddress + ", MAC: " +
                    macAddress + ", New MAC: " +
                    newMacAddress;
            list.add(currentRecord);
        }
        System.out.println("Map IPMac");
        for (String ip : mapIpMac.keySet()) {
            System.out.println("Key: " + ip + ", MAC: " + mapIpMac.get(ip));
        }
        return list;
    }
}
