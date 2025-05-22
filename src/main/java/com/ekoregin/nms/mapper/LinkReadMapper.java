package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Link;
import com.ekoregin.nms.dto.CustomerReadDto;
import com.ekoregin.nms.dto.DeviceReadDto;
import com.ekoregin.nms.dto.LinkReadDto;
import com.ekoregin.nms.dto.PortReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LinkReadMapper implements Mapper<Link, LinkReadDto> {

    private final DeviceReadMapper deviceReadMapper;
    private final CustomerReadMapper customerReadMapper;

    @Override
    public LinkReadDto map(Link link) {
        DeviceReadDto device = Optional.ofNullable(link.getDevice())
                .map(deviceReadMapper::map)
                .orElse(null);
        CustomerReadDto customer = Optional.ofNullable(link.getCustomer())
                .map(customerReadMapper::map)
                .orElse(null);

        return new LinkReadDto(
                link.getId(),
                link.getType(),
                link.getDestination(),
                device,
                customer
        );
    }
}
