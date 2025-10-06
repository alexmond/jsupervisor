package org.alexmond.jsupervisor.config;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Configuration class for supervisor settings")
@Component
@ConfigurationProperties(prefix = "supervisor")
@Validated
@Data
public class SupervisorConfig {

    /**
     * Collection of process configurations to be supervised.
     * Key is the process name, value is the process configuration.
     */
    @Schema(description = "Map of process configurations, where key is process name")
    Map<String, ProcessConfig> process = new HashMap<>();
    /**
     * The name of the supervisor node.
     * Defaults to "supervisor" if not specified.
     */
    @Schema(description = "The name of the supervisor node", example = "supervisor")
    private String nodeName = "supervisor";
    /**
     * Optional description of the supervisor node.
     */
    @Schema(description = "Description of the supervisor node", example = "Main process supervisor")
    private String description = "";

}
