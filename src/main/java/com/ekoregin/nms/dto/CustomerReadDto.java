package com.ekoregin.nms.dto;

import lombok.Value;

@Value
public class CustomerReadDto {
    Long id;
    String name;
    String address;
    String info;
    Double latitude;
    Double longitude;
}
