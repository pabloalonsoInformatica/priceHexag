package com.inditex.apps.platform.backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.inditex.apps.platform.backend", "com.inditex.contexts.platform"})
@EnableJpaRepositories(basePackages = "com.inditex.contexts.platform")
@EntityScan(basePackages = "com.inditex.contexts.platform.prices.infrastructure.persistence.h2")
public class PlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }

}
