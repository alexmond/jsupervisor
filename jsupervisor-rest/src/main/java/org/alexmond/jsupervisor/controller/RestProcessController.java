package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * REST Controller for managing and monitoring application processes.
 * Provides endpoints for starting, stopping, restarting, and retrieving process information.
 */
@Tag(name = "Supervisor", description = "Process API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestProcessController {

    final
    SupervisorConfig supervisorConfig;
    private final AtomicInteger counter = new AtomicInteger();
    private final ProcessRepository processRepository;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;

    /**
     * Starts all configured processes.
     *
     * @throws IOException if there is an error starting the processes
     */
    @PostMapping("/startAll")
    @Operation(summary = "Start all configured processes")
    @ApiResponse(responseCode = "200", description = "All processes started successfully")
    public void startAllProcess() throws IOException {
        processManagerBulk.startAll();
    }

    /**
     * Stops a specific process.
     *
     * @param name the name of the process to stop
     */
    @PostMapping("/stop/{name}")
    @Operation(summary = "Stop a specific process")
    @ApiResponse(responseCode = "200", description = "Process stopped successfully")
    public void stopProcess(@PathVariable String name) {
        processManager.stopProcess(name);
    }

    /**
     * Starts a specific process.
     *
     * @param name the name of the process to start
     */
    @PostMapping("/start/{name}")
    @Operation(summary = "Start a specific process")
    @ApiResponse(responseCode = "200", description = "Process started successfully")
    public void startProcess(@PathVariable String name) {
        processManager.startProcess(name);
    }

    /**
     * Restarts a specific process.
     *
     * @param name the name of the process to restart
     */
    @PostMapping("/restart/{name}")
    @Operation(summary = "Restart a specific process")
    @ApiResponse(responseCode = "200", description = "Process started successfully")
    @GetMapping("/restart/{name}")
    public void restartProcess(@PathVariable String name) {
        processManager.restartProcess(name);
    }

    /**
     * Retrieves details for a specific process.
     *
     * @param name the name of the process
     * @return the process status details
     */
    @PostMapping("/details/{name}")
    @Operation(summary = "Return the details for a specific process")
    @ApiResponse(responseCode = "200", description = "Details returned successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcessStatusRest.class)))
    public ProcessStatusRest processesDetails(@PathVariable String name) {
        return new ProcessStatusRest(name, processRepository.getRunningProcess(name));
    }


    /**
     * Retrieves all configured processes.
     *
     * @return a collection of all process statuses
     */
    @GetMapping("/allProcesses")
    @Operation(summary = "Get all configured processes")
    @ApiResponse(responseCode = "200", description = "List of all processes retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcessStatusRest.class)))
    public Collection<ProcessStatusRest> getAllProcesses() {
        return processRepository.findAllProcessStatusRest();
    }

}