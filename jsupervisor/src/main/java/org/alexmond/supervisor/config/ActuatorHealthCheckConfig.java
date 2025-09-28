package org.alexmond.supervisor.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ActuatorHealthCheckConfig extends AbstractHealthCheckConfig {
    private String actuatorHealthUrl;
}
