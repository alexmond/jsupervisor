package org.alexmond.jsupervisor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Configuration for port-based health checks.
 * This config allows monitoring of services by checking if a specific port is open and accessible.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Configuration for port-based health check monitoring")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortHealthCheckConfig extends AbstractHealthCheckConfig {
    /**
     * The host address to check
     */
    @Schema(description = "Host address to monitor", example = "localhost")
    private String host;

    /**
     * The port number to check
     */
    @Schema(description = "Port number to monitor", example = "8080")
    private Integer port;
}
