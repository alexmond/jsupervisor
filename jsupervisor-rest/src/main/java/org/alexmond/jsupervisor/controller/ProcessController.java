package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

/**
 * REST Controller for individual process management operations.
 * Provides endpoints for starting, stopping, and monitoring individual processes.
 */
@RestController
@RequestMapping("/api/v1/processes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Process Management", description = "APIs for managing individual processes")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ProcessController {

    private final ProcessManager processManager;
    private final ProcessRepository processRepository;

    @GetMapping
    @Operation(summary = "List all processes", description = "Retrieve status information for all configured processes")
    public ResponseEntity<Collection<ProcessStatusRest>> getAllProcesses() {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        return ResponseEntity.ok(processes);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get process details", description = "Retrieve detailed information about a specific process")
    public ResponseEntity<ProcessStatusRest> getProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        ProcessStatusRest process = processRepository.getRunningProcessRest(name);
        if (process == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(process);
    }

    @PostMapping("/{name}/start")
    @Operation(summary = "Start process", description = "Start a specific process")
    public ResponseEntity<Map<String, String>> startProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        try {
            processManager.startProcess(name);
            return ResponseEntity.ok(Map.of("message", "Process start initiated", "process", name));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{name}/stop")
    @Operation(summary = "Stop process", description = "Stop a specific process")
    public ResponseEntity<Map<String, String>> stopProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        ProcessStatusRest process = processRepository.getRunningProcessRest(name);
        if (process == null) {
            return ResponseEntity.notFound().build();
        }
        processManager.stopProcess(name);
        return ResponseEntity.ok(Map.of("message", "Process stop initiated", "process", name));
    }

    @PostMapping("/{name}/restart")
    @Operation(summary = "Restart process", description = "Restart a specific process")
    public ResponseEntity<Map<String, String>> restartProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        ProcessStatusRest process = processRepository.getRunningProcessRest(name);
        if (process == null) {
            return ResponseEntity.notFound().build();
        }
        processManager.restartProcess(name);
        return ResponseEntity.ok(Map.of("message", "Process restart initiated", "process", name));
    }

    @GetMapping("/{name}/status")
    @Operation(summary = "Get process status", description = "Get the current status of a specific process")
    public  ProcessStatusRest getProcessStatus(
            @Parameter(description = "Process name") @PathVariable String name) {
        return processRepository.getRunningProcessRest(name);
    }

    @GetMapping("/{name}/health")
    @Operation(summary = "Get process health", description = "Get the health status of a specific process")
    public ResponseEntity<Map<String, Object>> getProcessHealth(
            @Parameter(description = "Process name") @PathVariable String name) {
        ProcessStatusRest process = processRepository.getRunningProcessRest(name);
        if (process == null) {
            return ResponseEntity.notFound().build();
        }
        
        boolean isHealthy = process.getStatus().name().equals("healthy");
        return ResponseEntity.ok(Map.of(
            "name", process.getName(),
            "healthy", isHealthy,
            "status", process.getStatus(),
            "lastCheck", System.currentTimeMillis()
        ));
    }
}
