package org.alexmond.supervisor.config;

import lombok.Data;

import java.time.Duration;

@Data
public abstract class AbstractHealthCheckConfig {

    /**
     * Seconds to wait before the first probe.
     */
    private Integer initialDelaySeconds = 10;

    /**
     * How often (in seconds) to perform the probe.
     */
    private Integer periodSeconds = 10;

    /**
     * Seconds after which the probe times out.
     */
    private Integer timeoutSeconds = 10;

    /**
     * Minimum consecutive successes for the probe to be considered successful.
     */
    private Integer successThreshold = 2;

    /**
     * Number of consecutive failed probes before container restart.
     */
    private Integer failureThreshold = 2;
}
