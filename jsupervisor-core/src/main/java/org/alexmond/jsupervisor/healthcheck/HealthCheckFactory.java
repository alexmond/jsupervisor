package org.alexmond.jsupervisor.healthcheck;

import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.repository.RunningProcess;

public class HealthCheckFactory {
    public static HealthCheck getHealthCheck(ProcessConfig config, RunningProcess runningProcess) {
        return switch (config.getHealthCheckType()) {
            case ACTUATOR ->
                    config.getActuatorHealthCheck() != null ? new ActuatorHealthCheck(config.getActuatorHealthCheck(), runningProcess) : null;
            case HTTP ->
                    config.getHttpHealthCheckConfig() != null ? new HttpHealthCheck(config.getHttpHealthCheckConfig(), runningProcess) : null;
            case PORT ->
                    config.getPortHealthCheck() != null ? new PortHealthCheck(config.getPortHealthCheck(), runningProcess) : null;
            case NONE -> null;
            default -> null;
        };
    }
}
