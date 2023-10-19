package com.ekoregin.nms.dto;

import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {

    private Long id;

    private String name;

    private String description;

    private Inet ip;

    private String login;

    private String password;

    private Long modelId;
}
