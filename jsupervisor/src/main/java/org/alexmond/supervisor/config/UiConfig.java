package org.alexmond.supervisor.config;

import lombok.Data;
import org.alexmond.supervisor.model.ProcessStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration("uiconfig")
@ConfigurationProperties("ui-config")
@Data
public class UiConfig {
    Map<ProcessStatus,String> uiStatusClass = new HashMap<>();
}
