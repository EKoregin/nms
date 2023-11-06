package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.repository.ModelDeviceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ModelDeviceServiceImpl implements ModelDeviceService {

    private final ModelDeviceRepo modelDeviceRepo;

    @Override
    public ModelDevice create(ModelDeviceDto modelDeviceDto) {
        ModelDevice modelDevice;
        if (modelDeviceDto != null) {
            modelDevice = new ModelDevice(modelDeviceDto);
            modelDeviceRepo.save(modelDevice);
            log.info("ModelDevice with ID: {} was created", modelDeviceDto.getId());
        } else {
            log.warn("ModelDeviceDto is null");
            throw new NoSuchElementException("ModelDeviceDto is null");
        }
        return modelDevice;
    }

    @Override
    public void update(ModelDeviceDto modelDeviceDto) {
        ModelDevice modelDevice = findById(modelDeviceDto.getId());
        modelDevice.setType(modelDeviceDto.getType());
        modelDevice.setName(modelDeviceDto.getName());
        modelDevice.setManufacturer(modelDeviceDto.getManufacturer());
        modelDeviceRepo.save(modelDevice);
    }

    @Override
    public void update(ModelDevice modelDevice) {
        modelDeviceRepo.save(modelDevice);
    }

    @Override
    public ModelDevice findById(long id) {
        ModelDevice modelDevice = modelDeviceRepo.findById(id).orElse(null);
        if (modelDevice == null) {
            log.warn("ModelDevice with ID: {} not found!", id);
            throw new NoSuchElementException("ModelDevice with ID: " + id + " not found!");
        }
        return modelDevice;
    }

    @Override
    public List<ModelDevice> findAll() {
        return modelDeviceRepo.findAll(Sort.by("manufacturer"));
    }

    @Override
    public void delete(long id) {
        try {
            modelDeviceRepo.deleteById(id);
            log.info("ModelDevice with ID: {} was deleted", id);
        } catch (Exception e) {
            log.error("Не могу удалить модель с ID: {}", id);
        }
    }
}
