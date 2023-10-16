package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
