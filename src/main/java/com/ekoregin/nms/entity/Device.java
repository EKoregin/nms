package com.ekoregin.nms.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Device {
    private Long id;
    private String name;
    private String description;
    private ModelDevice model;
}