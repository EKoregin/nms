package com.ekoregin.nms.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Check {
    private Long id;
    private String name;
    private ModelDevice modelDevice;
}
