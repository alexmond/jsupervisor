package org.alexmond.supervisor.config;

import lombok.Data;
import org.alexmond.supervisor.model.ProcessStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class UiConfig {
    Map<ProcessStatus,String> uiStatusClass = new HashMap<>();
}
