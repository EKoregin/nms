package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.entity.ModelDevice;

import java.util.List;

public interface ModelDeviceService {
    ModelDevice create(ModelDeviceDto modelDeviceDto);

    void update(ModelDeviceDto modelDeviceDto);

    void update(ModelDevice modelDevice);

    void delete(long id);

    ModelDevice findById(long id);

    List<ModelDevice> findAll();
}
