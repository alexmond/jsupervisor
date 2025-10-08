package org.alexmond.jsupervisor.ui.config;

import lombok.Data;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for UI-specific settings.
 * Contains mappings and configuration properties used in the user interface.
 */
@Configuration("uiconfig")
@ConfigurationProperties("ui-config")
@Data
public class UiConfig {
    /**
     * Maps process status to corresponding Bootstrap CSS background classes.
     * Used for visual representation of process states in the UI.
     */
    Map<ProcessStatus, String> uiStatusClass = new HashMap<>() {{
        put(ProcessStatus.aborted, "bg-warning");
        put(ProcessStatus.failed, "bg-danger");
        put(ProcessStatus.failed_to_start, "bg-danger");
        put(ProcessStatus.finished, "bg-success");
        put(ProcessStatus.not_started, "bg-secondary");
        put(ProcessStatus.running, "bg-primary");
        put(ProcessStatus.starting, "bg-info");
        put(ProcessStatus.stopped, "bg-success");
        put(ProcessStatus.stopping, "bg-info");
        put(ProcessStatus.unknown, "bg-info");
        put(ProcessStatus.healthy, "bg-success");
        put(ProcessStatus.unhealthy, "bg-warning");
    }};
}
