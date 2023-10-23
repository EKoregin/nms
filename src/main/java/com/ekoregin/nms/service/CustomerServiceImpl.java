package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    @Override
    public Customer create(CustomerDto customerDto) {
        Customer customer;
        if (customerDto != null) {
            customer = new Customer(customerDto);
            customerRepo.save(customer);
            log.info("Customer with ID: {} was created", customer.getId());
        } else {
            log.warn("CustomerDto is null");
            throw new NoSuchElementException("CustomerDto is null");
        }
        return customer;
    }

    @Override
    public void update(CustomerDto customerDto) {
        Customer customer = findById(customerDto.getCustomerId());
        customer.setName(customerDto.getName());
        customer.setAddress(customerDto.getAddress());
        customerRepo.save(customer);
    }

    @Override
    public void update(Customer customer) {
        customerRepo.save(customer);
    }

    @Override
    public void delete(long id) {
        customerRepo.deleteById(id);
        log.info("Customer with ID: {} was deleted", id);
    }

    @Override
    public Customer findById(long id) {
        Customer customer = customerRepo.findById(id).orElse(null);
        if (customer == null) {
            log.warn("Customer with ID: {} not found!", id);
            throw new NoSuchElementException("Customer with ID: " + id + " not found!");
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }
}
