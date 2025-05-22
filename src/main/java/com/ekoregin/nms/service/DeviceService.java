package com.ekoregin.nms.service;

import com.ekoregin.nms.database.entity.*;
import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.database.repository.DeviceRepository;
import com.ekoregin.nms.dto.DeviceReadDto;
import com.ekoregin.nms.mapper.DeviceCreateEditMapper;
import com.ekoregin.nms.mapper.DeviceReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ModelDeviceService modelDeviceService;
    private final DeviceCreateEditMapper deviceCreateEditMapper;
    private final DeviceReadMapper deviceReadMapper;
    private final PortService portService;

    @Transactional
    public Device create(DeviceDto deviceDto) {
        Device device;
        if (deviceDto != null) {
            log.info("Try to create Device with name: {}, IP: {}, MAC: {}", deviceDto.getName(), deviceDto.getIp(), deviceDto.getMac());
            device = new Device(deviceDto);
            ModelDevice modelDevice = modelDeviceService.findById(deviceDto.getModelId());
            device.setModel(modelDevice);
            try {
                deviceRepository.save(device);
                log.info("Device {} with id: {} was created", device.getName(), device.getId());
                portService.createPortsForDeviceFromModel(device.getId());
            } catch (DataAccessException e) {
                log.error("Device {} was not created", device.getName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getLocalizedMessage(), e);
            }
        } else {
            log.warn("DeviceDto is null");
            throw new NoSuchElementException("DeviceDto is null");
        }
        return device;
    }

    @Transactional
    public Optional<DeviceReadDto> update(Long id, DeviceDto deviceDto) {
        log.info("Try to update Device with name: {}, IP: {}, MAC: {}", deviceDto.getName(), deviceDto.getIp(), deviceDto.getMac());
        return deviceRepository.findById(id)
                .map(entity -> deviceCreateEditMapper.map(deviceDto, entity))
                .map(deviceRepository::saveAndFlush)
                .map(deviceReadMapper::map);
    }

    @Transactional
    public void update(Device device) {
        deviceRepository.save(device);
    }

    @Transactional
    public void delete(long id) {
        deviceRepository.deleteById(id);
        log.info("Device with ID: {} was deleted", id);
    }

    public Device findById(long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            log.warn("Device with ID: {} not found!", id);
            throw new NoSuchElementException("Device with ID: " + id + " not found!");
        }
        return device;
    }

    public List<Device> findAll(String sortField) {
        return deviceRepository.findAll(Sort.by(sortField));
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public List<CheckDto> findAllChecksByDeviceId(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null)
            throw new RuntimeException("Device is null for deviceId " + deviceId);
        List<CheckDto> checkDtos = device.getModel().getChecks().stream()
                .map(CheckDto::new)
                .toList();
        checkDtos.stream().map(CheckDto::getCheckName).forEach(System.out::println);
        return checkDtos;
    }

    public DeviceDto findFreePortsByDeviceId(long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
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

        DeviceDto deviceDto = new DeviceDto(device);
        deviceDto.setCustomers(new ArrayList<>());
        deviceDto.setFreePorts(freePorts);
        return deviceDto;
    }

    public long count() {
        return deviceRepository.count();
    }

    public Page<Device> findPaginated(Pageable pageable, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        List<Device> devices = deviceRepository.findAll(Sort.by(sortField));
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Device> list;
        if (devices.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, devices.size());
            list = devices.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize, sort), devices.size());
    }

    public Page<Device> findByNamePaginated(String name, Pageable pageable) {
        List<Device> devices = deviceRepository.findDeviceCustomSearch(name, pageable);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Device> list;
        if (devices.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, devices.size());
            list = devices.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), devices.size());
    }
}
