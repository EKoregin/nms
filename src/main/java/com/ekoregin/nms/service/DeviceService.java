package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.Device;

import java.util.List;

public interface DeviceService {
    Device create(DeviceDto deviceDto);

    void update(DeviceDto deviceDto);

    void update(Device device);

    void delete(long id);

    Device findById(long id);

    List<Device> findAll(String sortField);

    List<Device> findAll();

    List<CheckDto> findAllChecksByDeviceId(long deviceId);

    DeviceDto findFreePortsByDeviceId(long deviceId);
}
