package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.Customer;
import lombok.Value;

import java.util.List;

@Value
public class DeviceReadDto {
    Long id;
    String name;
    String description;
    String ip;
    String mac;
    int managePort;
    String login;
    String password;
    Double latitude;
    Double longitude;
    Long modelId;
    List<Customer> customers;
    List<PortReadDto> ports;
}
