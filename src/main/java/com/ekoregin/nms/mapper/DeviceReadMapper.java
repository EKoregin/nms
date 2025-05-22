package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.repository.PortRepository;
import com.ekoregin.nms.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeviceReadMapper implements Mapper<Device, DeviceReadDto> {

    private final PortRepository portRepository;
    private final PortReadMapper portReadMapper;

    @Override
    public DeviceReadDto map(Device device) {
        return new DeviceReadDto(
                device.getId(),
                device.getName(),
                device.getDescription(),
                device.getIp().getAddress(),
                device.getMac(),
                device.getPort(),
                device.getLogin(),
                device.getPassword(),
                device.getLatitude(),
                device.getLongitude(),
                device.getModel().getId(),
                device.getCustomers(),
                getPorts(device.getId())
        );
    }

    private List<PortReadDto> getPorts(Long deviceId) {
        return portRepository.findAllByDeviceId(deviceId)
                .stream()
                .map(portReadMapper::map)
                .sorted(Comparator.comparing(PortReadDto::getName))
                .toList();
    }


}
