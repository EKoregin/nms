package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;

public interface CheckService {
    CheckResult checkExec(Customer customer, Device device, Check check);
}
