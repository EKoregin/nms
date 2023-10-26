package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorSnmp implements CheckExecutor {

    private final ModelDeviceService modelDeviceService;

    @Override
    public CheckResult checkExecute(Check check, Customer customer) {
        log.info("Starting check execute with SNMP");
        // Получаем модель на основании проверки
        ModelDevice modelDevice = modelDeviceService.findById(check.getModelDevice().getId());

        //Получаем устройство у которого совпадает модель
        Device checkDevice = customer.getDevices().stream()
                .filter(device -> device.getModel().equals(modelDevice))
                .findFirst().orElse(null);

        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        // Получаем необходимые для проверки параметры и сохраняем их в мапу
        Map<String, String> paramsForCheck = new HashMap<>();
        for (TypeTechParameter type : check.getTypeTechParams()) {
            String paramValue = customer.getParams().stream()
                    .filter(parameter -> parameter.getType().equals(type))
                    .map(TechParameter::getValue)
                    .findFirst().orElse(Strings.EMPTY);
            paramsForCheck.put(type.getName(), paramValue);
        }

        log.info("Params and values: {}", paramsForCheck);
        // Необходимо в конечном OID заменить пераметры типа {PORT} на значения, указанные у пользователя
        String snmpOID = updateVariablesInOIDwithValues(check.getSnmpOID(), paramsForCheck);
        Inet ipDevice = checkDevice.getIp();
        String community = checkDevice.getSnmpCommunity();

        return CheckResult.builder()
                .status(CheckResultStatus.OK)
                .id(1L)
                .result("IP: " + ipDevice.getAddress() + ", community: " + community + ", OID: " + snmpOID)
                .build();
    }

    private String updateVariablesInOIDwithValues(String sourceOID, Map<String, String> params) {
        String snmpOID = sourceOID;
        for (String param : params.keySet()) {
            snmpOID = snmpOID.replace("[#" + param + "]", params.get(param));
        }
        return snmpOID;
    }
}
