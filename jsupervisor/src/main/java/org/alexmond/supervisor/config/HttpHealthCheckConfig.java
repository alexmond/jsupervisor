package org.alexmond.supervisor.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class HttpHealthCheckConfig extends AbstractHealthCheckConfig {
    private String URL;
    private String method;
    private String returnCode;
    private List<String> headers;
}
