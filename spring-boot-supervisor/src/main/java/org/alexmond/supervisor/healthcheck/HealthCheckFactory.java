package org.alexmond.supervisor.healthcheck;

import org.alexmond.supervisor.config.ProcessConfig;
import org.springframework.stereotype.Component;

public class HealthCheckFactory {
    public static HealthCheck getHealthCheck(ProcessConfig config) {
        return switch (config.getHealthCheckType()) {
            case ACTUATOR ->
                    config.getActuatorHealthCheck() != null ? new ActuatorHealthCheck(config.getActuatorHealthCheck()) : null;
            case HTTP ->
                    config.getHttpHealthCheckConfig() != null ? new HttpHealthCheck(config.getHttpHealthCheckConfig()) : null;
            case PORT -> config.getPortHealthCheck() != null ? new PortHealthCheck(config.getPortHealthCheck()) : null;
            case NONE -> null;
            default -> null;
        };
    }
}
