package com.caceis.petstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PetstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetstoreApplication.class, args);
    }
}
