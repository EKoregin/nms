package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.Device;

import java.util.List;

public interface DeviceService {
    Device create(DeviceDto deviceDto);

    void update(DeviceDto deviceDto);

    void delete(long id);

    Device findById(long id);

    List<Device> findAll(String sortField);
}
