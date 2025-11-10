package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes started successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while starting processes"),
            @ApiResponse(responseCode = "409", description = "Some processes are already running")
    })
    public ResponseEntity<Void> startAll() {
        try {
            processManagerBulk.startAll();
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            log.error("Cannot start processes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Error starting processes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop all processes", description = "Stop all currently running processes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes stopped successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while stopping processes"),
            @ApiResponse(responseCode = "404", description = "No running processes found")
    })
    public ResponseEntity<Void> stopAll() {
        try {
            processManagerBulk.stopAll();
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            log.error("Cannot stop processes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error stopping processes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/restart")
    @Operation(summary = "Restart all processes", description = "Restart all configured processes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes restarted successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while restarting processes")
    })
    public ResponseEntity<Void> restartAll() {
        try {
            processManagerBulk.restartAll();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error restarting processes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
