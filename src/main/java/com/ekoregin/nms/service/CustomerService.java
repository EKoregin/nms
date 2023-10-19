package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer create(CustomerDto customerDto);

    void update(CustomerDto customerDto);

    void update(Customer customer);

    void delete(long id);

    Customer findById(long id);

    List<Customer> findAll();
}
