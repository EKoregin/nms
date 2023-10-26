package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
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
    private final SnmpClient snmpClient;

    @Override
    public CheckResult checkExecute(Check check, Customer customer) {
        log.info("Starting check execute with SNMP");
        Device checkDevice = getDeviceForCheck(check, customer);
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");
        Map<String, String> paramsForCheck = getParamsForCheck(check, customer);
        String snmpOID = updateVariablesInOIDwithValues(check.getSnmpOID(), paramsForCheck);
        String community = checkDevice.getSnmpCommunity();
        snmpClient.setIpAddress(checkDevice.getIp().getAddress());
        snmpClient.setPort(String.valueOf(checkDevice.getSnmpPort()));
        return snmpClient.snmpGet(community, snmpOID);
    }

    private Device getDeviceForCheck(Check check, Customer customer){
        ModelDevice modelDevice = modelDeviceService.findById(check.getModelDevice().getId());
        return customer.getDevices().stream()
                .filter(device -> device.getModel().equals(modelDevice))
                .findFirst().orElse(null);
    }

    private Map<String, String> getParamsForCheck(Check check, Customer customer) {
        Map<String, String> paramsForCheck = new HashMap<>();
        for (TypeTechParameter type : check.getTypeTechParams()) {
            String paramValue = customer.getParams().stream()
                    .filter(parameter -> parameter.getType().equals(type))
                    .map(TechParameter::getValue)
                    .findFirst().orElse(Strings.EMPTY);
            paramsForCheck.put(type.getName(), paramValue);
        }
        return paramsForCheck;
    }

    private String updateVariablesInOIDwithValues(String sourceOID, Map<String, String> params) {
        String snmpOID = sourceOID;
        for (String param : params.keySet()) {
            snmpOID = snmpOID.replace("[#" + param + "]", params.get(param));
        }
        return snmpOID;
    }
}
