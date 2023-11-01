package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findCustomersByNameContainsIgnoreCase(String textSearch, Pageable pageable);
}
