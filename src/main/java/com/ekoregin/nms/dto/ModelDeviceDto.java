package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.ModelDevice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelDeviceDto {

    private Long id;

    private String type;

    private String name;

    private String manufacturer;

    public ModelDeviceDto(ModelDevice modelDevice) {
        this.id = modelDevice.getId();
        this.type = modelDevice.getType();
        this.name = modelDevice.getName();
        this.manufacturer = modelDevice.getManufacturer();
    }
}
