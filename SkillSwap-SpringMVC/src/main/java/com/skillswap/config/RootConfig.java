package com.skillswap.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("com.skillswap")
public class RootConfig {

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");

        dataSource.setUrl(
                "jdbc:oracle:thin:@localhost:1521:orcljash"
        );

        dataSource.setUsername("SCOTT");

        dataSource.setPassword("tiger");

        return dataSource;
    }
}