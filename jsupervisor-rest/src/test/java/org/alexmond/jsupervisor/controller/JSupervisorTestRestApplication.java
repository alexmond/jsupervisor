package org.alexmond.jsupervisor.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles("test")
public class JSupervisorTestRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JSupervisorTestRestApplication.class, args);
    }

}
