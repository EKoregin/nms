package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
@Primary
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
//        Map<String, String> paramsForCheck = new HashMap<>();
//        for(TypeTechParameter type: check.getTypeTechParams()) {
//
//        }

        Inet ipDevice = checkDevice.getIp();
        String community = checkDevice.getSnmpCommunity();
        String snmpOID = check.getSnmpOID();

        return CheckResult.builder()
                .status(CheckResultStatus.OK)
                .id(1L)
                .result("IP: " + ipDevice.getAddress() + ", community: " + community + ", OID: " + snmpOID)
                .build();
    }
}
