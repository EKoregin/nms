package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.database.repository.ModelDeviceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ModelDeviceService {

    private final ModelDeviceRepo modelDeviceRepo;

    @Transactional
    public ModelDevice create(ModelDeviceDto modelDeviceDto) {
        ModelDevice modelDevice;
        if (modelDeviceDto != null) {
            log.info("Try to create ModelDevice with name: {}", modelDeviceDto.getName());
            modelDevice = new ModelDevice(modelDeviceDto);
            try {
                modelDeviceRepo.save(modelDevice);
                log.info("ModelDevice with ID: {} was created", modelDeviceDto.getId());
            } catch (DataAccessException e) {
                log.error("ModelDevice {} was not created", modelDevice.getName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getLocalizedMessage(), e);
            }

        } else {
            log.warn("ModelDeviceDto is null");
            throw new NoSuchElementException("ModelDeviceDto is null");
        }
        return modelDevice;
    }

    @Transactional
    public void update(ModelDeviceDto modelDeviceDto) {
        log.info("Try to update ModelDevice with name: {}", modelDeviceDto.getName());
        ModelDevice modelDevice = findById(modelDeviceDto.getId());
        modelDevice.setType(modelDeviceDto.getType());
        modelDevice.setName(modelDeviceDto.getName());
        modelDevice.setManufacturer(modelDeviceDto.getManufacturer());
        modelDevice.setNumberOfPorts(modelDeviceDto.getNumberOfPorts());
        modelDevice.setTypePort(modelDeviceDto.getTypePort());
        modelDevice.setControlMethods(modelDeviceDto.getControlMethods());
        modelDevice.setTypeTechParameters(modelDeviceDto.getTypeTechParameters());
        try {
            modelDeviceRepo.saveAndFlush(modelDevice);
            log.info("ModelDevice {} with id: {} was updated", modelDevice.getName(), modelDevice.getId());
        } catch (DataAccessException e) {
            log.error("Update ModelDevice {} error", modelDevice.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getLocalizedMessage(), e);
        }
    }

    @Transactional
    public void update(ModelDevice modelDevice) {
        modelDeviceRepo.saveAndFlush(modelDevice);
    }

    public ModelDevice findById(long id) {
        ModelDevice modelDevice = modelDeviceRepo.findById(id).orElse(null);
        if (modelDevice == null) {
            log.warn("ModelDevice with ID: {} not found!", id);
            throw new NoSuchElementException("ModelDevice with ID: " + id + " not found!");
        }
        return modelDevice;
    }

    public List<ModelDevice> findAll() {
        return modelDeviceRepo.findAll(Sort.by("manufacturer"));
    }

    @Transactional
    public void delete(long id) {
        try {
            modelDeviceRepo.deleteById(id);
            log.info("ModelDevice with ID: {} was deleted", id);
        } catch (Exception e) {
            log.error("Не могу удалить модель с ID: {}", id);
        }
    }
}
