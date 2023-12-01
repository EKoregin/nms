package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.repository.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomerWhenCustomerDtoNotNull() {
        Customer customer = new Customer();
        customer.setId(1L);
        doReturn(customer).when(customerRepo).save(any(Customer.class));
        var createResult = customerService.create(new CustomerDto());
        assertThat(createResult).isEqualTo(customer);
    }

    @Test
    void throwExceptionWhenCreateCustomerAndCustomerDtoIsNull() {
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.create(null));
        assertThat(exception.getMessage()).isEqualTo("CustomerDto is null");
    }
}