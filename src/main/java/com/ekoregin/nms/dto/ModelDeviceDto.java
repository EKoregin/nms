package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.entity.ModelDevice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelDeviceDto {

    private Long id;

    private String type;

    private String name;

    private String manufacturer;

    private List<Check> checks;

    private List<String> listDevices;

    public ModelDeviceDto(ModelDevice modelDevice) {
        this.id = modelDevice.getId();
        this.type = modelDevice.getType();
        this.name = modelDevice.getName();
        this.manufacturer = modelDevice.getManufacturer();
        this.checks = modelDevice.getChecks();
        this.listDevices = modelDevice.getDevices().stream().map(Device::getName).toList();
    }
}
