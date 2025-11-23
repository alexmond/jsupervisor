package org.alexmond.jsupervisor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@OpenAPIDefinition(
        info = @Info(
                title = "JSupervisor REST API",
                version = "0.0.2",
                description = """
                        JSupervisor is a process management and monitoring service that provides 
                        comprehensive control over application processes. This API enables you to:
                        
                        - Start, stop, and restart processes individually or in bulk
                        - Monitor process status and health
                        - Retrieve process logs and statistics
                        - Configure process behavior and auto-start settings
                        
                        The API follows RESTful principles and returns JSON responses.
                        """,
                contact = @Contact(
                        name = "Alex Mondshain",
                        email = "alex.mondshain@gmail.com",
                        url = "https://www.alexmond.org/jsupervisor/current/"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8086"
                )
        }
)
@SpringBootApplication
@ActiveProfiles("test")
public class JSupervisorTestRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JSupervisorTestRestApplication.class, args);
    }

}
