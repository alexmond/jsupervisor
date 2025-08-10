package org.alexmond.supervisor.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class PortHealthCheckConfig extends AbstractHealthCheckConfig {
    private String host;
    private Integer port;
}
