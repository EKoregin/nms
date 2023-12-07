package com.ekoregin.nms.integration.database.repository;

import com.ekoregin.nms.integration.IntegrationTestBase;
import com.ekoregin.nms.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class CustomerRepositoryTest extends IntegrationTestBase {

    private final CustomerRepo customerRepo;

    @Test
    void findById() {
       var actualCustomer =  customerRepo.findById(1L);
       actualCustomer.ifPresent(customer -> assertThat(customer.getId()).isEqualTo(1L));
    }
}
