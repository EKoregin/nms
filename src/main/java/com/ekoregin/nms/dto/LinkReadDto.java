package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.LinkDestination;
import com.ekoregin.nms.database.entity.LinkType;
import lombok.Value;

@Value
public class LinkReadDto {
    Long id;
    LinkType type;
    LinkDestination destination;
    DeviceReadDto device;
    CustomerReadDto customer;
}
