package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.repository.CustomerRepo;
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
@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    @Override
    public Customer create(CustomerDto customerDto) {
        Customer customer;
        if (customerDto != null) {
            customer = new Customer(customerDto);
            customer = customerRepo.save(customer);
            log.info("Customer with ID: {} was created", customer.getId());
        } else {
            log.warn("CustomerDto is null");
            throw new NoSuchElementException("CustomerDto is null");
        }
        return customer;
    }

    @Override
    public void update(CustomerDto customerDto) {
        if (customerDto == null)
            throw new NoSuchElementException("CustomerDto is null");
        Optional<Customer> customer = customerRepo.findById(customerDto.getCustomerId());
        if (customer.isEmpty())
            throw new NoSuchElementException("Customer with ID " + customerDto.getCustomerId() + " not found");
        customer.get().setName(customerDto.getName());
        customer.get().setAddress(customerDto.getAddress());
        customerRepo.save(customer.get());
    }

    @Override
    public void update(Customer customer) {
        if (customer == null)
            throw new NoSuchElementException("Customer is null");
        var foundCustomer = customerRepo.findById(customer.getId());
        if (foundCustomer.isEmpty()) {
            throw new NoSuchElementException("Customer with ID " + customer.getId() + " not found");
        }
        customerRepo.save(customer);
    }

    @Override
    public Customer findById(long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isEmpty()) {
            log.warn("Customer with ID: {} not found!", id);
            throw new NoSuchElementException("Customer with ID: " + id + " not found!");
        }
        return customer.get();
    }

    @Override
    public void delete(long id) {
        var foundCustomer = customerRepo.findById(id);
        if (foundCustomer.isEmpty()) {
            throw new NoSuchElementException("Customer with ID " + id + " not found");
        }
        customerRepo.delete(foundCustomer.get());
        log.info("Customer with ID: {} was deleted", id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    public long count() {
        return customerRepo.count();
    }

    @Override
    public Page<Customer> findPaginated(Pageable pageable, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        List<Customer> customers = customerRepo.findAll(Sort.by(sortField));
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

    @Override
    public Page<Customer> findByNamePaginated(String name, Pageable pageable) {
        List<Customer> customers = customerRepo.findCustomersByNameContainsIgnoreCase(name, pageable);
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
