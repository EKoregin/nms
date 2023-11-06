package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GatherDataService {

    private static CustomerService customerService;

    public void getMacByIP(Device device, Check check) {
        List<Customer> customers = customerService.findAll();

    }
}
