package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.dto.ModelDeviceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelDeviceCreateEditMapper implements Mapper<ModelDeviceDto, ModelDevice> {
    @Override
    public ModelDevice map(ModelDeviceDto modelDeviceDto) {
        ModelDevice modelDevice = new ModelDevice();
        copy(modelDeviceDto, modelDevice);
        return modelDevice;
    }

    @Override
    public ModelDevice map(ModelDeviceDto fromObject, ModelDevice toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(ModelDeviceDto object, ModelDevice modelDevice) {
        modelDevice.setId(object.getId());
        modelDevice.setType(object.getType());
        modelDevice.setName(object.getName());
        modelDevice.setManufacturer(object.getManufacturer());
        modelDevice.setNumberOfPorts(object.getNumberOfPorts());
        modelDevice.setControlMethods(object.getControlMethods());
        modelDevice.setTypePort(object.getTypePort());
        modelDevice.setTypeTechParameters(object.getTypeTechParameters());
    }
}
