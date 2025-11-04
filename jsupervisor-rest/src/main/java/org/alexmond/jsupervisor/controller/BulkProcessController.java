package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for bulk process management operations.
 * Provides endpoints for managing multiple processes at once.
 */
@RestController
@RequestMapping("/api/v1/processes/bulk")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bulk Process Operations", description = "APIs for managing multiple processes simultaneously")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BulkProcessController {

    private final ProcessManagerBulk processManagerBulk;
    private final ProcessRepository processRepository;

    @PostMapping("/start")
    @Operation(summary = "Start all processes", description = "Start all configured processes that are not currently running")
    public ResponseEntity<Map<String, Object>> startAll() {
        Collection<ProcessStatusRest> beforeStart = processRepository.findAllProcessStatusRest();
        List<String> stoppedProcesses = beforeStart.stream()
                .filter(p -> !p.isAlive())
                .map(ProcessStatusRest::getName)
                .collect(Collectors.toList());

        processManagerBulk.startAll();

        return ResponseEntity.ok(Map.of(
                "message", "Bulk start initiated",
                "processesToStart", stoppedProcesses,
                "count", stoppedProcesses.size()
        ));
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop all processes", description = "Stop all currently running processes")
    public ResponseEntity<Map<String, Object>> stopAll() {
        try {
            Collection<ProcessStatusRest> beforeStop = processRepository.findAllProcessStatusRest();
            List<String> runningProcesses = beforeStop.stream()
                    .filter(ProcessStatusRest::isAlive)
                    .map(ProcessStatusRest::getName)
                    .collect(Collectors.toList());

            processManagerBulk.stopAll();

            return ResponseEntity.ok(Map.of(
                    "message", "Bulk stop initiated",
                    "processesToStop", runningProcesses,
                    "count", runningProcesses.size()
            ));
        } catch (IOException e) {
            log.error("Failed to stop all processes", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to stop processes: " + e.getMessage()));
        }
    }

    @PostMapping("/restart")
    @Operation(summary = "Restart all processes", description = "Restart all configured processes")
    public ResponseEntity<Map<String, Object>> restartAll() {
        try {
            Collection<ProcessStatusRest> allProcesses = processRepository.findAllProcessStatusRest();
            List<String> allProcessNames = allProcesses.stream()
                    .map(ProcessStatusRest::getName)
                    .collect(Collectors.toList());

            processManagerBulk.stopAll();
            // Small delay to ensure processes are stopped
            Thread.sleep(2000);
            processManagerBulk.startAll();

            return ResponseEntity.ok(Map.of(
                    "message", "Bulk restart initiated",
                    "processes", allProcessNames,
                    "count", allProcessNames.size()
            ));
        } catch (Exception e) {
            log.error("Failed to restart all processes", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to restart processes: " + e.getMessage()));
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get bulk status", description = "Get summary status of all processes")
    public ResponseEntity<Map<String, Object>> getBulkStatus() {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();

        long running = processes.stream().filter(ProcessStatusRest::isAlive).count();
        long stopped = processes.size() - running;
        long healthy = processes.stream().filter(p -> "healthy".equals(p.getStatus().name())).count();
        long unhealthy = processes.stream().filter(p -> "unhealthy".equals(p.getStatus().name())).count();

        return ResponseEntity.ok(Map.of(
                "total", processes.size(),
                "running", running,
                "stopped", stopped,
                "healthy", healthy,
                "unhealthy", unhealthy,
                "timestamp", System.currentTimeMillis()
        ));
    }
}
