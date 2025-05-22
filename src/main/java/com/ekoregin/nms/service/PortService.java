package com.ekoregin.nms.service;

import com.ekoregin.nms.database.entity.*;
import com.ekoregin.nms.database.repository.DeviceRepository;
import com.ekoregin.nms.database.repository.ModelDeviceRepo;
import com.ekoregin.nms.database.repository.PortRepository;
import com.ekoregin.nms.dto.PortCreateEditDto;
import com.ekoregin.nms.dto.PortReadDto;
import com.ekoregin.nms.mapper.PortCreateEditMapper;
import com.ekoregin.nms.mapper.PortReadMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PortService {

    private final PortRepository portRepository;
    private final PortCreateEditMapper portCreateEditMapper;
    private final PortReadMapper portReadMapper;
    private final ModelDeviceService modelDeviceService;
    private final DeviceRepository deviceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PortReadDto create(PortCreateEditDto portDto) {
        log.info("Create port in service");
        return Optional.of(portDto)
                .map(portCreateEditMapper::map)
                .map(portRepository::save)
                .map(portReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public PortReadDto update(Long id, PortCreateEditDto portDto) {
        log.info("Update port with ID: " + id);
        return portRepository.findById(id)
                .map(entity -> portCreateEditMapper.map(portDto, entity))
                .map(portRepository::saveAndFlush)
                .map(portReadMapper::map)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<PortReadDto> loadPortsByDeviceId(Long deviceId) {
        return portRepository.findAllByDeviceId(deviceId).stream()
                .map(portReadMapper::map)
                .toList();
    }

    @Transactional
    public void createBunchOfPorts(Long modelId, int portNumbers) {
        ModelDevice model = modelDeviceService.findById(modelId);
        for (int i = 0; i < portNumbers; i++) {
            Port port = Port.builder()
                    .name(String.valueOf(i + 1))
                    .speed(100)
                    .duplex(PortDuplex.FULL)
                    .mediaType(MediaType.COPPER)
                    .type(PortType.PHYSICAL)
                    .modelDevice(model)
                    .build();
            portRepository.save(port);
        }
    }

    public PortReadDto findById(Long portId) {
        return portRepository.findById(portId)
                .map(portReadMapper::map)
                .orElseThrow(() -> new NoSuchElementException("Port with id: " + portId + " not found"));
    }

    @Transactional
    public boolean delete(Long id) {
       return portRepository.findById(id)
                .map(entity -> {
                    portRepository.delete(entity);
                    portRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void createPortsForDeviceFromModel(Long deviceId) {
        var foundDevice = deviceRepository.findById(deviceId)
                .orElseThrow();
        log.info("Device: {}", foundDevice.getName());
        var modelDevicePorts = foundDevice.getModel().getPorts();

        for (Port port : modelDevicePorts) {
            entityManager.detach(port);
            port.setId(null);
            port.setDevice(foundDevice);
            port.setModelDevice(null);
            portRepository.save(port);
        }
    }
}
