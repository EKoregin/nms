package com.ekoregin.nms.integration.database.repository;

import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.integration.IntegrationTestBase;
import com.ekoregin.nms.database.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class CustomerRepositoryTest extends IntegrationTestBase {

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    @Test
    void delete() {
        var maybeCustomer =  customerRepository.findById(DEFAULT_CUSTOMER_ID);
        assertTrue(maybeCustomer.isPresent());
        maybeCustomer.ifPresent(customerRepository::delete);
        entityManager.flush();
        assertTrue(customerRepository.findById(DEFAULT_CUSTOMER_ID).isEmpty());
    }

    @Test
    void findById() {
       var actualCustomer =  customerRepository.findById(1L);
       assertNotNull(actualCustomer);
       actualCustomer.ifPresent(customer -> assertThat(customer.getId()).isEqualTo(1L));
       actualCustomer.ifPresent(customer -> assertThat(customer.getName()).isEqualTo("default_customer"));
       actualCustomer.ifPresent(customer -> assertThat(customer.getAddress()).isEqualTo("default_address"));
    }

    @Test
    void save() {
        var customer = Customer.builder()
                .name("Test")
                .address("Test address")
                .build();
        entityManager.persist(customer);
        assertNotNull(customer.getId());
    }
}
