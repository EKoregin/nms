package com.ekoregin.nms.dto;


import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.entity.CustomerDevice;
import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.entity.TechParameter;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long customerId;
    private String name;
    private String address;
    private String info;
    private Double latitude;
    private Double longitude;
    private List<TechParameter> params;
    private List<Device> devices;
    private Set<CustomerDevice> connections;

    public CustomerDto(Customer customer) {
        customerId = customer.getId();
        name = customer.getName();
        address = customer.getAddress();
        info = customer.getInfo();
        params = customer.getParams();
        devices = customer.getDevices();
        latitude = customer.getLatitude();
        longitude = customer.getLongitude();
    }
}
