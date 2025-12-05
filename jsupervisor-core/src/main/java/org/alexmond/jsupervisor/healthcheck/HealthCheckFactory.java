package org.alexmond.jsupervisor.healthcheck;

import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.model.RunningProcess;

/**
 * Factory class responsible for creating appropriate HealthCheck instances based on process configuration.
 */
public class HealthCheckFactory {
    /**
     * Creates and returns a HealthCheck instance based on the provided process configuration.
     *
     * @param config         The process configuration containing health check settings
     * @param runningProcess The running process instance that will be monitored
     * @return HealthCheck instance corresponding to the configured type, or null if no health check is configured
     */
    public static HealthCheck getHealthCheck(ProcessConfig config, RunningProcess runningProcess) {
        return switch (config.getHealthCheckType()) {
            case ACTUATOR ->
                    config.getActuatorHealthCheck() != null ? new ActuatorHealthCheck(config.getActuatorHealthCheck(), runningProcess) : null;
            case HTTP ->
                    config.getHttpHealthCheckConfig() != null ? new HttpHealthCheck(config.getHttpHealthCheckConfig(), runningProcess) : null;
            case PORT ->
                    config.getPortHealthCheck() != null ? new PortHealthCheck(config.getPortHealthCheck(), runningProcess) : null;
            default -> null;
        };
    }
}
