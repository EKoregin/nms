package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.repository.CustomerRepository;
import com.ekoregin.nms.mapper.CustomerCreateEditMapper;
import com.ekoregin.nms.mapper.CustomerReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final Long CUSTOMER_ID = 99L;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerCreateEditMapper customerCreateEditMapper;

    @Mock
    private CustomerReadMapper customerReadMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerWhenCustomerDtoNotNull() {
        Customer customer = getCustomer();
        CustomerDto customerDto = getCustomerDto();
        doReturn(customer).when(customerRepository).save(any(Customer.class));

        var createResult = customerService.create(customerDto);

        assertThat(createResult).isEqualTo(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void throwExceptionWhenCreateCustomerAndCustomerDtoIsNull() {
        var exception = assertThrows(NoSuchElementException.class, () -> customerService.create(null));
        assertThat(exception.getMessage()).isEqualTo("CustomerDto is null");
    }

//    @Test
//    void shouldUpdateExistedCustomerDto() {
//        CustomerDto customerDto = getCustomerDto();
//        customerDto.setName("Ivanov Ivan Petrovich");
//        customerDto.setAddress("Moscow");
//        Customer customer = getCustomer();
//        doReturn(Optional.of(customer)).when(customerRepository).findById(customerDto.getCustomerId());
//        customer.setName("Ivanov Ivan Petrovich");
//        customer.setAddress("Moscow");
////        doReturn(customer).when(customerCreateEditMapper.map(customerDto, new Customer()));
//
//        customerService.update(CUSTOMER_ID, customerDto);
//
//        verify(customerRepository).saveAndFlush(customer);
//    }

//    @Test
//    void throwExceptionIfCustomerNotFoundWhenUpdate() {
//        CustomerDto customerDto = getCustomerDto();
//        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(99L, customerDto));
//        assertThat(exception.getMessage()).isEqualTo("Customer with ID " + customerDto.getCustomerId() + " not found");
//    }

//    @Test
//    void throwExceptionIfCustomerDtoIsNullWhenUpdate() {
//        CustomerDto customerDto = null;
//        var exception = assertThrows(NoSuchElementException.class, () -> customerService.update(null,customerDto));
//        assertThat(exception.getMessage()).isEqualTo("CustomerDto is null");
//    }

    @Test
    void shouldUpdateIfCustomerIsExisted() {
        Customer customer = getCustomer();
        doReturn(Optional.of(customer)).when(customerRepository).findById(customer.getId());

        customerService.update(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void throwExceptionIfUpdateCustomerNotFound() {
        Customer customer = getCustomer();
        customer.setId(100500L);
        doReturn(Optional.empty()).when(customerRepository).findById(customer.getId());

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
        doReturn(Optional.of(customer)).when(customerRepository).findById(customer.getId());

        assertTrue(customerService.delete(customer.getId()));

        verify(customerRepository).delete(customer);
    }

    @Test
    void getFalseWhenCustomerNotFoundWhenDelete() {
        Customer customer = getCustomer();
        doReturn(Optional.empty()).when(customerRepository).findById(customer.getId());
        assertFalse(customerService.delete(customer.getId()));
    }

    Customer getCustomer () {
        return Customer.builder()
                .id(CUSTOMER_ID)
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }

    CustomerDto getCustomerDto() {
        return CustomerDto.builder()
                .customerId(CUSTOMER_ID)
                .name("Ivanov Ivan Ivanovich")
                .address("Pokrov")
                .build();
    }
}