package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.ModelDevice;
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
    private final ModelDeviceService modelDeviceService;
    private final CustomerService customerService;
    private final CheckExecutor checkExecutorSnmp;
    private final CheckExecutor checkExecutorTelnet;
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
    public CheckResult execute(long checkId, long customerId) {
        Check foundCheck = findById(checkId);
        Customer foundCustomer = customerService.findById(customerId);
        CheckResult checkResult;
        switch (foundCheck.getCheckType()) {
            case "SNMP" -> checkResult = checkExecutorSnmp.checkExecute(foundCheck, foundCustomer);
            case "TELNET" -> checkResult = checkExecutorTelnet.checkExecute(foundCheck, foundCustomer);
            case "MIKROTIK_API" -> checkResult = checkExecutorMikrotik.checkExecute(foundCheck, foundCustomer);
            default -> throw new IllegalArgumentException("That method not support!");
        }
        return checkResult;
    }
}
