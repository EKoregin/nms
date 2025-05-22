package com.ekoregin.nms.integration;

import com.ekoregin.nms.integration.annotation.IT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.0");
    private static final MySQLContainer<?> container2 = new MySQLContainer<>("mysql:8.0");

    @BeforeAll
    static void runContainer() {
        container.start();
        container2.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
        container2.stop();
    }

    @AfterEach
    void afterEach() {

    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("db.mikrobill.url", container2::getJdbcUrl);
    }
}
