package org.alexmond.jsupervisor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Configuration for HTTP-based health checks.
 * Extends basic health check configuration with HTTP-specific properties.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Configuration for HTTP-based health checks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpHealthCheckConfig extends AbstractHealthCheckConfig {
    /**
     * The URL to send health check requests to
     */
    @Schema(description = "URL endpoint for health check", example = "http://localhost:8080/health")
    private String url;

    /**
     * HTTP method to use for the health check request
     */
    @Schema(description = "HTTP method for health check", example = "GET")
    private String method;

    /**
     * Expected HTTP return code for successful health check
     */
    @Schema(description = "Expected HTTP status code", example = "200")
    private String returnCode;

    /**
     * Custom HTTP headers to include in the health check request
     */
    @Schema(description = "Custom HTTP headers for health check request")
    private List<String> headers;
}
