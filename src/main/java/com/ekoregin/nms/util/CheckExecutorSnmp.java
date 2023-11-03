package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
    public CheckResult checkExecute(Check check, Customer customer, Device device) {
        Device checkDevice = null;

        Map<String, String> paramsForCheck = new HashMap<>();
        if (check.getCheckScope().equals(CheckScope.CUSTOMER.name())) {
            log.info("Starting check execute with SNMP for customer");
            checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
            paramsForCheck = getParamsForCheck(check, customer);
        } else if (check.getCheckScope().equals(CheckScope.DEVICE.name())) {
            log.info("Starting check execute with SNMP for device");
            checkDevice = device;
        }
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        String snmpOID = replacingVariablesWithValues(check.getSnmpOID(), paramsForCheck);
        String community = checkDevice.getSnmpCommunity();
        snmpClient.setIpAddress(checkDevice.getIp().getAddress());
        snmpClient.setPort(String.valueOf(checkDevice.getSnmpPort()));

        String substRules = check.getSubstRules();
        CheckResult checkResult = snmpClient.snmpGet(community, snmpOID);
        if (substRules != null && !substRules.isEmpty()) {
            log.info("Substitution rules: " + substRules);
            addSubstitutionToCheckResult(substRules, checkResult);
        }
        return checkResult;
    }

    private void addSubstitutionToCheckResult(String substRules, CheckResult checkResult) {
        Map<String, String> substRulesMap = new HashMap<>();
        for (String keyValue : substRules.split(";")) {
            String[] parts = keyValue.split("=");
            substRulesMap.put(parts[0], parts[1]);
        }
        String updResult = checkResult.getResult();
        for (String value : substRulesMap.keySet()) {
            updResult = updResult.replace(value, substRulesMap.get(value));
        }
        checkResult.setResult(updResult);
    }
}
