package org.alexmond.supervisor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;


/**
 * Configuration class for supervisor settings.
 * Maps properties with 'supervisor' prefix from configuration files
 * to this class fields.
 */
@Component
@ConfigurationProperties(prefix = "supervisor")
@Validated
@Data
public class SupervisorConfig {

    private String nodeName = "supervisor";
    private String description = "";

    /**
     * Collection of process configurations to be supervised
     */
   Map<String,ProcessConfig> process = new HashMap<>();

}
