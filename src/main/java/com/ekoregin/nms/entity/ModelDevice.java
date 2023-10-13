package com.ekoregin.nms.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ModelDevice {
    private Long id;
    private TypeDevice type;
    private String name;
    private String manufacturer;
}

enum TypeDevice {
    SWITCH, PON, ROUTER
}
