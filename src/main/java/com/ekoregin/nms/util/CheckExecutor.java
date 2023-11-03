package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;

public interface CheckExecutor {

    CheckResult checkExecute(Check check, Customer customer, Device device);

    default Device getDeviceForCheck(ModelDeviceService modelDeviceService, Check check, Customer customer){
        ModelDevice modelDevice = modelDeviceService.findById(check.getModelDevice().getId());
        return customer.getDevices().stream()
                .filter(device -> device.getModel().equals(modelDevice))
                .findFirst().orElse(null);
    }

    default Map<String, String> getParamsForCheck(Check check, Customer customer) {
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

    default String replacingVariablesWithValues(String sourceOID, Map<String, String> params) {
        String snmpOID = sourceOID;
        for (String param : params.keySet()) {
            snmpOID = snmpOID.replace("[#" + param + "]", params.get(param));
        }
        return snmpOID;
    }
}
