package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class CheckExecutor {

    private Check check;
    private Customer customer;

    private final ModelDeviceService modelDeviceService;

    public CheckResult execute() {
        // Получаем модели у которых есть нужная проверка
        List<ModelDevice> models = modelDeviceService.findAll().stream()
                .filter(model -> model.getChecks().contains(check))
                .toList();

        // Получаем устройство абонента у которого совпадает модель
        Device checkDevice = null;
        for (ModelDevice model: models) {
            checkDevice = customer.getDevices().stream()
                    .filter(device -> device.getModel().equals(model))
                    .findFirst().orElse(null);
        }

        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        // Получаем данные необходимые для выполнения проверки
        Inet ipDevice = checkDevice.getIp();
        String community = checkDevice.getSnmpCommunity();
        String snmpOID = check.getSnmpOID();
        //Список необходимых параметров
        List<TechParameter> parameters;

        return new CheckResult();
    }
}
