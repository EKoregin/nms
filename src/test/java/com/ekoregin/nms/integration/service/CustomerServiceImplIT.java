package com.ekoregin.nms.integration.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.integration.IntegrationTestBase;
import com.ekoregin.nms.repository.CustomerRepo;
import com.ekoregin.nms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class CustomerServiceImplIT extends IntegrationTestBase {

    private final CustomerService customerService;
    private final CustomerRepo customerRepo;

    @BeforeEach
    void clearDatabase() {
        customerRepo.deleteAll();
        System.out.println("Delete all records in table customer");
    }


    @Test
    void shouldGetCustomers() {
        customerService.create(CustomerDto.builder()
                .name("Ivanov")
                .address("Pokrov")
                .build());
        customerService.create(CustomerDto.builder()
                .name("Petrov")
                .address("Kovrov")
                .build());

        List<Customer> customers = customerService.findAll();
        assertThat(customers).hasSize(2);
    }

    @Test
    void findById() {
        var createdCustomer = customerService.create(getCustomerDto());
        var actualCustomer =  customerService.findById(createdCustomer.getId());
        assertThat(actualCustomer.getId()).isEqualTo(createdCustomer.getId());
    }

    @Test
    void create() {
        CustomerDto customerDto = getCustomerDto();
        Customer customer = getCustomer();
        var actualCustomer = customerService.create(customerDto);

        assertThat(customer.getName()).isEqualTo(actualCustomer.getName());
    }

    CustomerDto getCustomerDto() {
        return CustomerDto.builder()
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }

    Customer getCustomer () {
        return Customer.builder()
                .id(99L)
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }
}
