package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @Min(value = 0, message = "Значение не может быть меньше нуля")
    @Max(value = 500, message = "Значение не может быть больше 500")
    @Digits(integer = 10, fraction = 0, message = "Некорректный ввод")
    private int numberOfPorts;

    private List<CheckTypeEntity> controlMethods = new ArrayList<>();

    private long typePortId;

    private List<Check> checks;

    private List<String> listDevices;



    public ModelDeviceDto(ModelDevice modelDevice) {
        this.id = modelDevice.getId();
        this.type = modelDevice.getType();
        this.name = modelDevice.getName();
        this.manufacturer = modelDevice.getManufacturer();
        this.numberOfPorts = modelDevice.getNumberOfPorts();
        this.typePortId = modelDevice.getTypePort().getId();
        this.checks = modelDevice.getChecks();
        this.listDevices = modelDevice.getDevices().stream().map(Device::getName).toList();
        this.controlMethods = modelDevice.getControlMethods();
    }
}
