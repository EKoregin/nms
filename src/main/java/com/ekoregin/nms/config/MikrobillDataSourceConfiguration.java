package com.ekoregin.nms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MikrobillDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("db.mikrobill")
    public DataSourceProperties mikrobillDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource mikrobillDataSource() {
        return mikrobillDataSourceProperties()
                .initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate mikrobillJdbcTemplate(@Qualifier("mikrobillDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
