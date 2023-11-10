package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.repository.DeviceRepo;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

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
            try {
                deviceRepo.saveAndFlush(device);
            } catch (ConstraintViolationException e) {
//                log.error("Ошибка создания оборудования {}", deviceDto.getName());
//                throw new RuntimeException("Ошибка создания оборудования :" + deviceDto.getName());
            }
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
        device.setIp(new Inet(deviceDto.getIp()));
        device.setMac(deviceDto.getMac());
        device.setPort(deviceDto.getManagePort());
        device.setLogin(deviceDto.getLogin());
        device.setPassword(deviceDto.getPassword());
        device.setModel(modelDeviceService.findById(deviceDto.getModelId()));
        try {
            deviceRepo.save(device);
        } catch (Exception e) {
            log.error("Ошибка обновления оборудования с ID: {}", deviceDto.getId());
            throw new RuntimeException();
        }
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
    public List<Device> findAll(String sortField) {
        return deviceRepo.findAll(Sort.by(sortField));
    }

    @Override
    public List<Device> findAll() {
        return deviceRepo.findAll();
    }

    @Override
    public List<CheckDto> findAllChecksByDeviceId(long deviceId) {
        Device device = deviceRepo.findById(deviceId).orElse(null);
        if (device == null)
            throw new RuntimeException("Device is null for deviceId " + deviceId);
        List<CheckDto> checkDtos = device.getModel().getChecks().stream()
                .map(CheckDto::new)
                .toList();
        checkDtos.stream().map(CheckDto::getCheckName).forEach(System.out::println);
        return checkDtos;
    }

    @Override
    public List<Integer> findFreePortsByDeviceId(long deviceId) {
        Device device = deviceRepo.findById(deviceId).orElse(null);
        if (device == null)
            throw new RuntimeException("Device is null for deviceId " + deviceId);

        TypeTechParameter typePort = device.getModel().getTypePort();

        int numberOfPorts = device.getModel().getNumberOfPorts();
        List<Integer> freePorts = new ArrayList<>(IntStream.rangeClosed(1, numberOfPorts).boxed().toList());
        List<Customer> customers = device.getCustomers();
        List<Integer> busyPorts = customers.stream()
                .map(Customer::getParams)
                .flatMap(Collection::stream)
                .filter(techParameter -> techParameter.getType().equals(typePort))
                .map(TechParameter::getValue)
                .map(Integer::parseInt).toList();
        for (Integer busyPort : busyPorts) {
            freePorts.remove(busyPort);
        }
        return freePorts;
    }
}
