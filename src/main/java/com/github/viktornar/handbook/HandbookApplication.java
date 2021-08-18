package com.github.viktornar.handbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(HandbookProperties.class)
@EnableTransactionManagement
public class HandbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandbookApplication.class, args);
    }
}
