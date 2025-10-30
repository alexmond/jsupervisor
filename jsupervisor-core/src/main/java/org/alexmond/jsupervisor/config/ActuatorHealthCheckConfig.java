package org.alexmond.jsupervisor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Configuration for Spring Boot Actuator health check endpoint.
 * This class extends the base health check configuration with Actuator-specific settings.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Configuration for Spring Boot Actuator health check endpoint")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActuatorHealthCheckConfig extends AbstractHealthCheckConfig {

    /**
     * The URL of the Actuator health endpoint to check.
     * This should be the complete URL to the health endpoint (e.g., "http://localhost:8080/actuator/health")
     */
    @Schema(description = "URL of the Actuator health endpoint",
            example = "http://localhost:8080/actuator/health")
    private String actuatorHealthUrl;
}
