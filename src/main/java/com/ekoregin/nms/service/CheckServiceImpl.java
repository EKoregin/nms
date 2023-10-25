package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.repository.CheckRepo;
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
}
