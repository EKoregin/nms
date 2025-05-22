package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Port;
import com.ekoregin.nms.dto.DeviceReadDto;
import com.ekoregin.nms.dto.LinkReadDto;
import com.ekoregin.nms.dto.PortReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PortReadMapper implements Mapper<Port, PortReadDto> {

//    private final DeviceReadMapper deviceReadMapper;
//    private final LinkReadMapper linkReadMapper;

    @Override
    public PortReadDto map(Port port) {
//        DeviceReadDto device = Optional.ofNullable(port.getDevice())
//                .map(deviceReadMapper::map)
//                .orElse(null);
//        LinkReadDto link = Optional.ofNullable(port.getLink())
//                .map(linkReadMapper::map)
//                .orElse(null);
        String linkName = Optional.ofNullable(port.getLink())
                .map(link -> link.getCustomer().getName())
                .orElse("");
        return new PortReadDto(
                port.getId(),
                port.getName(),
                port.getSpeed(),
                port.getDuplex(),
                port.getMediaType(),
                port.getType(),
                linkName
        );
    }
}
