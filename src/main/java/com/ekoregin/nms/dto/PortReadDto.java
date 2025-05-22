package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.*;
import lombok.Value;

@Value
public class PortReadDto {
    Long id;
    String name;
    Integer speed;
    PortDuplex duplex;
    MediaType mediaType;
    PortType type;
    String linkName;
//    LinkReadDto link;
}
