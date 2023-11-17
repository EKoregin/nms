package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.repository.CheckRepo;
import com.ekoregin.nms.util.CheckExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CheckServiceImpl implements CheckService{

    private final CheckRepo checkRepo;
    private final DeviceService deviceService;
    private final ModelDeviceService modelDeviceService;
    private final CustomerService customerService;
    private final CheckExecutor checkExecutorSnmp;
    private final CheckExecutor checkExecutorTelnet;
    private final CheckExecutor checkExecutorRest;
    private final CheckExecutor checkExecutorMikrotik;

    @Override
    public Check create(CheckDto checkDto) {
        Check check;
        if (checkDto != null) {
            ModelDevice modelDevice = modelDeviceService.findById(checkDto.getModelDeviceId());
            check = new Check(checkDto);
            check.setModelDevice(modelDevice);
            log.info("Check with ID: {} was created", checkRepo.save(check).getCheckId());
        } else {
            log.warn("CheckDto is null");
            throw new NoSuchElementException("CheckDto is null");
        }
        return check;
    }

    @Override
    public void update(CheckDto checkDto) {
        Check check = findById(checkDto.getCheckId());
        check.setCheckType(checkDto.getCheckType());
        check.setCheckName(checkDto.getCheckName());
        check.setSnmpOID(checkDto.getSnmpOID());
        check.setDescription(checkDto.getDescription());
        check.setSubstRules(checkDto.getSubstRules());
        check.setTelnetCommands(checkDto.getTelnetCommands());
        check.setJsonFilter(checkDto.getJsonFilter());
        check.setRegexFilter(checkDto.getRegexFilter());
        check.setCheckScope(checkDto.getCheckScope());
        checkRepo.save(check);
    }

    @Override
    public void update(Check check) {
        checkRepo.save(check);
    }

    @Override
    public void delete(long checkId) {
        checkRepo.deleteById(checkId);
        log.info("Check with ID: {} was deleted", checkId);
    }

    @Override
    public Check findById(long checkId) {
        Check check = checkRepo.findById(checkId).orElse(null);
        if (check == null) {
            log.warn("Check with ID: {} not found!", checkId);
            throw new NoSuchElementException("Check with ID: " + checkId + " not found!");
        }
        return check;
    }

    @Override
    public List<Check> findAll() {
        return checkRepo.findAll();
    }

    @Override
    public CheckResult executeForCustomer(long checkId, long customerId) {
        Check foundCheck = findById(checkId);
        Customer foundCustomer = customerService.findById(customerId);
        CheckResult checkResult;
        switch (foundCheck.getCheckType()) {
            case "SNMP" -> checkResult = checkExecutorSnmp.checkExecute(foundCheck, foundCustomer, null);
            case "TELNET" -> checkResult = checkExecutorTelnet.checkExecute(foundCheck, foundCustomer, null);
            case "REST_API" -> checkResult = checkExecutorRest.checkExecute(foundCheck, foundCustomer, null);
            default -> throw new IllegalArgumentException("That method not support!");
        }
        return checkResult;
    }

    @Override
    public CheckResult executeForDevice(long checkId, long deviceId) {
        Check foundCheck = findById(checkId);
        Device foundDevice = deviceService.findById(deviceId);
        CheckResult checkResult;
        switch (foundCheck.getCheckType()) {
            case "SNMP" -> checkResult = checkExecutorSnmp.checkExecute(foundCheck, null, foundDevice);
            case "TELNET" -> checkResult = checkExecutorTelnet.checkExecute(foundCheck, null, foundDevice);
            case "REST_API" -> checkResult = checkExecutorRest.checkExecute(foundCheck, null, foundDevice);
            case "MIKROTIK_API" -> checkResult = checkExecutorMikrotik.checkExecute(foundCheck, null, foundDevice);
            default -> throw new IllegalArgumentException("That method not support!");
        }
        return checkResult;
    }
}
