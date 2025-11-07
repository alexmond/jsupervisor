// Java
package org.alexmond.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthStatusResponse {
    private String status; // overall
    private Map<String, ComponentStatus> components;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ComponentStatus {
        private String status;   // component status
        private JsonNode details; // keep generic; we’ll bind specific ones as needed
    }
}
