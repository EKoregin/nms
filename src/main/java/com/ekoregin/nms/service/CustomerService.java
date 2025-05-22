package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.repository.CustomerRepository;
import com.ekoregin.nms.dto.CustomerReadDto;
import com.ekoregin.nms.mapper.CustomerCreateEditMapper;
import com.ekoregin.nms.mapper.CustomerReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerCreateEditMapper customerCreateEditMapper;
    private final CustomerReadMapper customerReadMapper;

    @Transactional
    public Customer create(CustomerDto customerDto) {
        Customer customer;
        if (customerDto != null) {
            customer = new Customer(customerDto);
            customer = customerRepository.save(customer);
            log.info("Customer with ID: {} was created", customer.getId());
        } else {
            log.warn("CustomerDto is null");
            throw new NoSuchElementException("CustomerDto is null");
        }
        return customer;
    }

    @Transactional
    public Optional<CustomerReadDto> update(Long id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .map(entity -> customerCreateEditMapper.map(customerDto, entity))
                .map(customerRepository::saveAndFlush)
                .map(customerReadMapper::map);
    }

    @Transactional
    public void update(Customer customer) {
        if (customer == null)
            throw new NoSuchElementException("Customer is null");
        var foundCustomer = customerRepository.findById(customer.getId());
        if (foundCustomer.isEmpty()) {
            throw new NoSuchElementException("Customer with ID " + customer.getId() + " not found");
        }
        customerRepository.save(customer);
    }

    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            log.warn("Customer with ID: {} not found!", id);
            throw new NoSuchElementException("Customer with ID: " + id + " not found!");
        }
        return customer.get();
    }

    @Transactional
    public boolean delete(long id) {
        return customerRepository.findById(id)
                .map(entity -> {
                    customerRepository.delete(entity);
                    customerRepository.flush();
                    log.info("Customer with ID: {} was deleted", id);
                    return true;
                })
                .orElse(false);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public long count() {
        return customerRepository.count();
    }

    public Page<Customer> findPaginated(Pageable pageable, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        List<Customer> customers = customerRepository.findAll(Sort.by(sortField));
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Customer> list;
        if (customers.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, customers.size());
            list = customers.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize, sort), customers.size());
    }

    public Page<Customer> findByNamePaginated(String name, Pageable pageable) {
        List<Customer> customers = customerRepository.findCustomersByNameContainsIgnoreCase(name, pageable);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Customer> list;
        if (customers.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, customers.size());
            list = customers.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), customers.size());
    }
}
