package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.database.repository.PortRepository;
import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.dto.PortReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModelDeviceReadDtoMapper implements Mapper<ModelDevice, ModelDeviceDto> {

    private final PortRepository portRepository;
    private final PortReadMapper portReadMapper;


    @Override
    public ModelDeviceDto map(ModelDevice modelDevice) {
        return new ModelDeviceDto(
                modelDevice.getId(),
                modelDevice.getType(),
                modelDevice.getName(),
                modelDevice.getManufacturer(),
                modelDevice.getNumberOfPorts(),
                modelDevice.getControlMethods(),
                modelDevice.getTypePort(),
                modelDevice.getTypeTechParameters(),
                modelDevice.getChecks().stream().map(CheckDto::new).toList(),
                modelDevice.getDevices().stream().map(Device::getName).toList(),
                getPorts(modelDevice.getId())
        );
    }

    private List<PortReadDto> getPorts(Long modelDeviceId) {
        return portRepository.findAllByModelDeviceId(modelDeviceId)
                .stream()
                .map(portReadMapper::map)
                .sorted(Comparator.comparing(PortReadDto::getName))
                .toList();
    }


}
