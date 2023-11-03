package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import java.util.List;

public interface CheckService {
    Check create(CheckDto checkDto);

    void update(CheckDto checkDto);

    void update(Check check);

    void delete(long checkId);

    Check findById(long checkId);

    List<Check> findAll();

    CheckResult executeForCustomer(long checkId, long customerId);

    CheckResult executeForDevice(long checkId, long deviceId);
}
