package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST Controller for supervisor system information.
 * Provides endpoints for system monitoring and supervisor status.
 */
@RestController
@RequestMapping("/api/v1/supervisor")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supervisor Information", description = "APIs for supervisor system information and monitoring")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SupervisorController {

    private final SupervisorConfig supervisorConfig;
//    private final SupervisorRest supervisorRest;

    @GetMapping("/info")
    @Operation(summary = "Get supervisor info", description = "Retrieve basic supervisor information")
    public ResponseEntity<Map<String, Object>> getSupervisorInfo() {
        return ResponseEntity.ok(Map.of(
                "nodeName", supervisorConfig.getNodeName(),
                "description", supervisorConfig.getDescription(),
                "processCount", supervisorConfig.getProcess().size(),
                "version", getClass().getPackage().getImplementationVersion() != null ?
                        getClass().getPackage().getImplementationVersion() : "dev"
        ));
    }

//    @GetMapping("/system")
//    @Operation(summary = "Get system information", description = "Retrieve system hardware and OS information")
//    public ResponseEntity<SupervisorRest> getSystemInfo() {
//        supervisorRest.refresh(); // Update available memory
//        return ResponseEntity.ok(supervisorRest);
//    }

    @GetMapping("/health")
    @Operation(summary = "Supervisor health check", description = "Check if supervisor is healthy")
    public ResponseEntity<Map<String, Object>> getHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "supervisor", supervisorConfig.getNodeName(),
                "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/configuration")
    @Operation(summary = "Get configuration", description = "Retrieve current supervisor configuration (without sensitive data)")
    public ResponseEntity<Map<String, Object>> getConfiguration() {
        return ResponseEntity.ok(Map.of(
                "nodeName", supervisorConfig.getNodeName(),
                "description", supervisorConfig.getDescription(),
                "processNames", supervisorConfig.getProcess().keySet(),
                "enabled", supervisorConfig.isEnabled()
        ));
    }
}
