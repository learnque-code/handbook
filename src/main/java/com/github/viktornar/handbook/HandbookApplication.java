package com.github.viktornar.handbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(HandbookProperties.class)
public class HandbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(HandbookApplication.class, args);
    }
}
