package org.alexmond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles("test")
public class JSupervisorCoreTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JSupervisorCoreTestApplication.class, args);
    }

}
