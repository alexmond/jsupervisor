package org.alexmond.jsupervisor.cli;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("js-cli")
public class JSupervisorCliConfig {
    String url;
    String username;
    String password;
}
