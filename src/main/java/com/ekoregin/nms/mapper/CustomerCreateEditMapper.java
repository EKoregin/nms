package com.ekoregin.nms.mapper;

import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerCreateEditMapper implements Mapper<CustomerDto, Customer> {
    @Override
    public Customer map(CustomerDto object) {
        Customer customer = new Customer();
        copy(object, customer);
        return customer;
    }

    @Override
    public Customer map(CustomerDto fromObject, Customer toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(CustomerDto object, Customer customer) {
        customer.setId(object.getCustomerId());
        customer.setName(object.getName());
        customer.setAddress(object.getAddress());
        customer.setInfo(object.getInfo());
        customer.setLatitude(object.getLatitude());
        customer.setLongitude(object.getLongitude());
    }
}
