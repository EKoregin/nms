package com.ekoregin.nms.service;

import com.ekoregin.nms.database.entity.*;
import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.database.repository.CheckRepository;
import com.ekoregin.nms.mapper.CustomerCreateEditMapper;
import com.ekoregin.nms.util.CheckExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheckService {

    private final CheckRepository checkRepository;
    private final DeviceService deviceService;
    private final ModelDeviceService modelDeviceService;
    private final CustomerService customerService;
    private final CheckExecutor checkExecutorSnmp;
    private final CheckExecutor checkExecutorTelnet;
    private final CheckExecutor checkExecutorRest;
    private final CheckExecutor checkExecutorMikrotik;
    private final CustomerCreateEditMapper customerCreateEditMapper;

    @Transactional
    public Check create(CheckDto checkDto) {
        Check check;
        if (checkDto != null) {
            ModelDevice modelDevice = modelDeviceService.findById(checkDto.getModelDeviceId());
            check = new Check(checkDto);
            check.setModelDevice(modelDevice);
            log.info("Check with ID: {} was created", checkRepository.save(check).getCheckId());
        } else {
            log.warn("CheckDto is null");
            throw new NoSuchElementException("CheckDto is null");
        }
        return check;
    }

    @Transactional
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
        check.setCreator(checkDto.getIsCreator());
        check.setForConnecting(checkDto.isForConnecting());
        check.setForDisconnecting(checkDto.isForDisconnecting());
        checkRepository.save(check);
    }

    @Transactional
    public void update(Check check) {
        checkRepository.save(check);
    }

    @Transactional
    public void delete(long checkId) {
        checkRepository.deleteById(checkId);
        log.info("Check with ID: {} was deleted", checkId);
    }

    public Check findById(long checkId) {
        Check check = checkRepository.findById(checkId).orElse(null);
        if (check == null) {
            log.warn("Check with ID: {} not found!", checkId);
            throw new NoSuchElementException("Check with ID: " + checkId + " not found!");
        }
        return check;
    }

    public Optional<Check> findByModelDeviceAndForConnecting(ModelDevice modelDevice) {
        return checkRepository.findByModelDeviceAndForConnectingIs(modelDevice, true);
    }

    public Optional<Check> findByModelDeviceAndForDisconnecting(ModelDevice modelDevice) {
        return checkRepository.findByModelDeviceAndForDisconnectingIs(modelDevice, true);
    }

    public List<Check> findAll() {
        return checkRepository.findAll();
    }

    public CheckResult executeForCustomer(long checkId, long customerId) {
        Check foundCheck = findById(checkId);
        Customer foundCustomer = customerService.findById(customerId);
//        Customer foundCustomer = customerService.findById(customerId)
//                .map(customerCreateEditMapper::map)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CheckResult checkResult;
        log.info("Type of check {} for customer {}", foundCheck.getCheckType(), foundCustomer.getName());
        switch (foundCheck.getCheckType()) {
            case "SNMP" -> checkResult = checkExecutorSnmp.checkExecute(foundCheck, foundCustomer, null);
            case "TELNET" -> checkResult = checkExecutorTelnet.checkExecute(foundCheck, foundCustomer, null);
            case "REST_API" -> checkResult = checkExecutorRest.checkExecute(foundCheck, foundCustomer, null);
            case "MIKROTIK_API" -> checkResult = checkExecutorMikrotik.checkExecute(foundCheck, foundCustomer, null);
            default -> throw new IllegalArgumentException("That method not support!");
        }
        return checkResult;
    }

    public CheckResult executeForDevice(long checkId, long deviceId) {
        Check foundCheck = findById(checkId);
        Device foundDevice = deviceService.findById(deviceId);
        CheckResult checkResult;
        log.info("Type of check {} for device {}", foundCheck.getCheckType(), foundDevice.getName());
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
