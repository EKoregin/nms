package com.ekoregin.nms.dto;


import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.entity.TechParameter;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long customerId;

    private String name;

    private String address;

    private List<TechParameter> params;

    private List<Device> devices;

    public CustomerDto(Customer customer) {
        customerId = customer.getId();
        name = customer.getName();
        address = customer.getAddress();
        params = customer.getParams();
        devices = customer.getDevices();
    }
}
