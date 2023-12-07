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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomerWhenCustomerDtoNotNull() {
        Customer customer = getCustomer();
        CustomerDto customerDto = getCustomerDto();
        doReturn(customer).when(customerRepo).save(any(Customer.class));

        var createResult = customerService.create(customerDto);

        assertThat(createResult).isEqualTo(customer);
        verify(customerRepo).save(customer);
    }

    @Test
    void throwExceptionWhenCreateCustomerAndCustomerDtoIsNull() {
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.create(null));
        assertThat(exception.getMessage()).isEqualTo("CustomerDto is null");
    }

    @Test
    void shouldUpdateExistedCustomerDto() {
        CustomerDto customerDto = getCustomerDto();
        customerDto.setName("Ivanov Ivan Petrovich");
        customerDto.setAddress("Moscow");
        Customer customer = getCustomer();
        doReturn(Optional.of(customer)).when(customerRepo).findById(customerDto.getCustomerId());
        customer.setName("Ivanov Ivan Petrovich");
        customer.setAddress("Moscow");

        customerService.update(customerDto);

        verify(customerRepo).save(customer);
    }

    @Test
    void throwExceptionIfCustomerNotFoundWhenUpdate() {
        CustomerDto customerDto = getCustomerDto();
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(customerDto));
        assertThat(exception.getMessage()).isEqualTo("Customer with ID " + customerDto.getCustomerId() + " not found");
    }

    @Test
    void throwExceptionIfCustomerDtoIsNullWhenUpdate() {
        CustomerDto customerDto = null;
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(customerDto));
        assertThat(exception.getMessage()).isEqualTo("CustomerDto is null");
    }

    @Test
    void shouldUpdateIfCustomerIsExisted() {
        Customer customer = getCustomer();
        doReturn(Optional.of(customer)).when(customerRepo).findById(customer.getId());

        customerService.update(customer);

        verify(customerRepo).save(customer);
    }

    @Test
    void throwExceptionIfUpdateCustomerNotFound() {
        Customer customer = getCustomer();
        customer.setId(100500L);
        doReturn(Optional.empty()).when(customerRepo).findById(customer.getId());

        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(customer));
        assertThat(exception.getMessage()).isEqualTo("Customer with ID " + customer.getId() + " not found");
    }

    @Test
    void throwExceptionIfCustomerIsNullWhenUpdate() {
        Customer customer = null;
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(customer));
        assertThat(exception.getMessage()).isEqualTo("Customer is null");
    }

    @Test
    void shouldDeleteExistedCustomer() {
        Customer customer = getCustomer();
        doReturn(Optional.of(customer)).when(customerRepo).findById(customer.getId());

        customerService.delete(customer.getId());

        verify(customerRepo).delete(customer);
    }

    @Test
    void throwExceptionWhenCustomerNotFoundWhenDelete() {
        Customer customer = getCustomer();
        doReturn(Optional.empty()).when(customerRepo).findById(customer.getId());
        assertThrows(NoSuchElementException.class, () -> customerService.delete(customer.getId()));
    }

    Customer getCustomer () {
        return Customer.builder()
                .id(99L)
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }

    CustomerDto getCustomerDto() {
        return CustomerDto.builder()
                .customerId(99L)
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }
}