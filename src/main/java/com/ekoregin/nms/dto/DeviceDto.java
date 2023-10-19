package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.Device;
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

    public DeviceDto(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.description = device.getDescription();
        this.ip = device.getIp();
        this.login = device.getLogin();
        this.password = device.getPassword();
        this.modelId = device.getModel().getId();
    }
}
