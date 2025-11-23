package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.service.ProcessGroupManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for bulk process management operations.
 * Provides endpoints for managing multiple processes at once.
 */
@RestController
@RequestMapping("/api/v1/processes/group")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Group Process Operations", description = "APIs for managing multiple processes simultaneously")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GroupProcessController {

    private final ProcessGroupManager processGroupManager;

    @PostMapping("/start")
    @Operation(summary = "Start all processes", description = "Start all configured processes that are not currently running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes started successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while starting processes"),
            @ApiResponse(responseCode = "409", description = "Some processes are already running")
    })
    public ResponseEntity<Void> startAll() {
        processGroupManager.startAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop all processes", description = "Stop all currently running processes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes stopped successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while stopping processes"),
            @ApiResponse(responseCode = "404", description = "No running processes found")
    })
    public ResponseEntity<Void> stopAll() {
        processGroupManager.stopAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restart")
    @Operation(summary = "Restart all processes", description = "Restart all configured processes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All processes restarted successfully"),
            @ApiResponse(responseCode = "500", description = "Error occurred while restarting processes")
    })
    public ResponseEntity<Void> restartAll() {
        processGroupManager.restartAll();
        return ResponseEntity.ok().build();
    }
}
