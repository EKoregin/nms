package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.repository.DeviceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepo deviceRepo;
    private final ModelDeviceService modelDeviceService;

    @Override
    public Device create(DeviceDto deviceDto) {
        Device device;
        if (deviceDto != null) {
            long modelId = deviceDto.getModelId();
            ModelDevice modelDevice = modelDeviceService.findById(modelId);
            device = new Device(deviceDto);
            device.setModel(modelDevice);
            deviceRepo.save(device);
            log.info("Device with ID: {} was created", deviceDto.getId());
        } else {
            log.warn("DeviceDto is null");
            throw new NoSuchElementException("DeviceDto is null");
        }
        return device;
    }

    @Override
    public void update(DeviceDto deviceDto) {
        Device device = findById(deviceDto.getId());
        device.setName(deviceDto.getName());
        device.setDescription(deviceDto.getDescription());
        device.setIp(deviceDto.getIp());
        device.setLogin(deviceDto.getLogin());
        device.setPassword(deviceDto.getPassword());
        device.setModel(modelDeviceService.findById(deviceDto.getModelId()));
        deviceRepo.save(device);
    }

    @Override
    public void delete(long id) {
        deviceRepo.deleteById(id);
        log.info("Device with ID: {} was deleted", id);
    }

    @Override
    public Device findById(long id) {
        Device device = deviceRepo.findById(id).orElse(null);
        if (device == null) {
            log.warn("Device with ID: {} not found!", id);
            throw new NoSuchElementException("Device with ID: " + id + " not found!");
        }
        return device;
    }

    @Override
    public List<Device> findAll() {
        return deviceRepo.findAll();
    }
}
