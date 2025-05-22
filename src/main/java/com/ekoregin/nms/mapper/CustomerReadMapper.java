package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.dto.CustomerReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerReadMapper implements Mapper<Customer, CustomerReadDto> {
    @Override
    public CustomerReadDto map(Customer object) {
        return new CustomerReadDto(
                object.getId(),
                object.getName(),
                object.getAddress(),
                object.getInfo(),
                object.getLatitude(),
                object.getLongitude()
        );
    }
}
