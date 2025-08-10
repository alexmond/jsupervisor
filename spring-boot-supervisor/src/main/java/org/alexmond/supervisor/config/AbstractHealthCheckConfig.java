package org.alexmond.supervisor.config;

import lombok.Data;

import java.time.Duration;

@Data
public abstract class AbstractHealthCheckConfig {

    /**
     * Seconds to wait before the first probe.
     */
    private Integer initialDelaySeconds;

    /**
     * How often (in seconds) to perform the probe.
     */
    private Integer periodSeconds;

    /**
     * Seconds after which the probe times out.
     */
    private Integer timeoutSeconds;

    /**
     * Minimum consecutive successes for the probe to be considered successful.
     */
    private Integer successThreshold;

    /**
     * Number of consecutive failed probes before container restart.
     */
    private Integer failureThreshold;
}
