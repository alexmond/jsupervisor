package org.alexmond.jsupervisor.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Abstract base class providing common configuration properties for health checks.
 * Defines timing and threshold parameters used to control health check behavior.
 * This class serves as a foundation for specific health check implementations.
 */
@Data
@Schema(description = "Base configuration for health checks")
public abstract class AbstractHealthCheckConfig {

    /**
     * Seconds to wait before the first probe.
     */
    @Schema(description = "Seconds to wait before the first probe", example = "10")
    private Integer initialDelaySeconds = 10;

    /**
     * How often (in seconds) to perform the probe.
     */
    @Schema(description = "How often (in seconds) to perform the probe", example = "10")
    private Integer periodSeconds = 10;

    /**
     * Seconds after which the probe times out.
     */
    @Schema(description = "Seconds after which the probe times out", example = "10")
    private Integer timeoutSeconds = 10;

    /**
     * Whether to ignore SSL certificate validation errors during health checks.
     */
    @Schema(description = "Whether to ignore SSL certificate validation errors during health checks", example = "false")
    private boolean ignoreSslErrors = false;

    /**
     * Minimum consecutive successes for the probe to be considered successful.
     */
    @Schema(description = "Minimum consecutive successes for the probe to be considered successful", example = "2")
    private Integer successThreshold = 2;

    /**
     * Number of consecutive failed probes before container restart.
     */
    @Schema(description = "Number of consecutive failed probes before container restart", example = "2")
    private Integer failureThreshold = 2;
}
