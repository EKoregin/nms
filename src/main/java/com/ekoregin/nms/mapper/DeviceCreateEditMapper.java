package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.database.repository.ModelDeviceRepo;
import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.service.ModelDeviceService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceCreateEditMapper implements Mapper<DeviceDto, Device> {

    private final ModelDeviceRepo modelDeviceRepo;

    @Override
    public Device map(DeviceDto object) {
        Device device = new Device();
        copy(object, device);
        return device;
    }

    @Override
    public Device map(DeviceDto fromObject, Device toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(DeviceDto deviceDto, Device device) {
        device.setName(deviceDto.getName());
        device.setDescription(deviceDto.getDescription());
        device.setIp(new Inet(deviceDto.getIp()));
        device.setMac(deviceDto.getMac());
        device.setPort(deviceDto.getManagePort());
        device.setLogin(deviceDto.getLogin());
        device.setPassword(deviceDto.getPassword());
        device.setModel(getModelDevice(deviceDto.getModelId()));
        device.setLatitude(deviceDto.getLatitude());
        device.setLongitude(deviceDto.getLongitude());
    }

    private ModelDevice getModelDevice(Long deviceId) {
        return Optional.ofNullable(deviceId)
                .flatMap(modelDeviceRepo::findById)
                .orElse(null);
    }
}
