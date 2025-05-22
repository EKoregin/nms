package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.entity.Link;
import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.database.entity.Port;
import com.ekoregin.nms.database.repository.DeviceRepository;
import com.ekoregin.nms.database.repository.LinkRepository;
import com.ekoregin.nms.database.repository.ModelDeviceRepo;
import com.ekoregin.nms.dto.PortCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PortCreateEditMapper implements Mapper<PortCreateEditDto, Port> {

    private final DeviceRepository deviceRepository;
    private final ModelDeviceRepo modelDeviceRepository;
    private final LinkRepository linkRepository;

    @Override
    public Port map(PortCreateEditDto object) {
        Port port = new Port();
        copy(object, port);
        return port;
    }

    @Override
    public Port map(PortCreateEditDto fromObject, Port toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(PortCreateEditDto fromObject, Port toPort) {
        toPort.setName(fromObject.getName());
        toPort.setSpeed(fromObject.getSpeed());
        toPort.setDuplex(fromObject.getDuplex());
        toPort.setMediaType(fromObject.getMediaType());
        toPort.setType(fromObject.getType());
        toPort.setDevice(getDevice(fromObject.getDeviceId()));
        toPort.setModelDevice(getModel(fromObject.getModelId()));
    }

    private Device getDevice(Long deviceId) {
        return Optional.ofNullable(deviceId)
                .flatMap(deviceRepository::findById)
                .orElse(null);
    }

    private ModelDevice getModel(Long modelId) {
        return Optional.ofNullable(modelId)
                .flatMap(modelDeviceRepository::findById)
                .orElse(null);
    }

    private Link getLink(Long linkId) {
        return Optional.ofNullable(linkId)
                .flatMap(linkRepository::findById)
                .orElse(null);
    }
}
