package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.entity.TypeTechParameter;
import com.ekoregin.nms.repository.TechParameterRepo;
import com.ekoregin.nms.repository.TypeTechParameterRepo;
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
    private final TechParameterRepo techParameterRepo;
    private final TypeTechParameterRepo ttpRepo;

    public List<String> getMacByIP(long deviceId, long checkId) {
        List<Customer> customers = customerService.findAll();
        String resultString = checkService.executeForDevice(checkId, deviceId).getResult();
        JsonElement jsonElement = JsonParser.parseString(resultString);

        Map<String, String> mapIpMac = new HashMap<>();
        List<String> listForReport = new ArrayList<>();

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            if (!element.getAsJsonObject().isJsonNull()) {
                String status = String.valueOf(element.getAsJsonObject().get("status")).replaceAll("\"", "");
                String activeIpAddress = String.valueOf(element.getAsJsonObject().get("active-address")).replaceAll("\"", "");
                String activeMacAddress = String.valueOf(element.getAsJsonObject().get("active-mac-address")).replaceAll("\"", "");

                if (status.equals("bound")) {
                    mapIpMac.put(activeIpAddress, activeMacAddress);
                }
            }
        }

        long recCounter = 1;
        TypeTechParameter ttpMac = ttpRepo.findByName("MAC");

        for (Customer customer : customers) {
            StringBuilder stringForReport = new StringBuilder(recCounter++ + ": " + customer.getName() + ", IP: ");

            TechParameter ipAddress = customer.getParams().stream()
                    .filter(param -> param.getType().getName().equals("IP"))
                    .findFirst().orElse(null);

            TechParameter macAddress = customer.getParams().stream()
                    .filter(param -> param.getType().getName().equals("MAC"))
                    .findFirst().orElse(null);

            if (ipAddress == null) {
                log.warn("IP адрес для абонента {} не определен", customer.getName());
                stringForReport.append("не определен");
            } else {
                String ipAddressValue = ipAddress.getValue();
                stringForReport.append(ipAddressValue).append(", MAC: ");
                // Если такой IP есть в найденных DHCP связках, то добавляем или обновляем MAC-адрес
                if (mapIpMac.containsKey(ipAddressValue)) {
                    String newMacAddressValue = mapIpMac.get(ipAddressValue);
                    // Если у абонента нет мак адреса, то создаем новый параметр и добавляем абоненту.
                    if (macAddress == null) {
                        if (newMacAddressValue != null) {
                            macAddress = TechParameter.builder()
                                    .value(newMacAddressValue)
                                    .type(ttpMac)
                                    .customer(customer)
                                    .build();
                            stringForReport.append("не определен, New MAC: ").append(newMacAddressValue);
                            techParameterRepo.save(macAddress);
                        } else {
                            stringForReport.append("не найдена DHCP связка!");
                        }
                    } else { // Если мак есть и он другой, то обновляем значение
                        if (!macAddress.getValue().equals(newMacAddressValue)) {
                            stringForReport.append(" -> MAC ИЗМЕНИЛСЯ!");
                            stringForReport.append(macAddress.getValue()).append(" ,New MAC: ");
                            macAddress.setValue(newMacAddressValue);
                            stringForReport.append(newMacAddressValue);
                            techParameterRepo.save(macAddress);
                        }
                    }
                }
            }
            listForReport.add(stringForReport.toString());
        }
        return listForReport;
    }
}
