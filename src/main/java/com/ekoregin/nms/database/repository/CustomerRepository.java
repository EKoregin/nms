package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findCustomersByNameContainsIgnoreCase(String textSearch, Pageable pageable);
}
