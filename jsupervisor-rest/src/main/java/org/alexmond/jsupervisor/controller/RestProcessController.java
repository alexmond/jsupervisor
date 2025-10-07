package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Tag(name = "Supervisor", description = "Process API")
@RestController
@RequestMapping("/api/v1")
public class RestProcessController {

    final
    SupervisorConfig supervisorConfig;
    private final AtomicInteger counter = new AtomicInteger();
    private final ProcessRepository processRepository;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;

    @Autowired
    public RestProcessController(ProcessRepository processRepository, SupervisorConfig supervisorConfig, ProcessManager processManager, ProcessManagerBulk processManagerBulk) {
        this.processRepository = processRepository;
        this.supervisorConfig = supervisorConfig;
        this.processManager = processManager;
        this.processManagerBulk = processManagerBulk;
    }

    @PostMapping("/startAll")
    @Operation(summary = "Start all configured processes")
    @ApiResponse(responseCode = "200", description = "All processes started successfully")
    public void startAllProcess() throws IOException {
        processManagerBulk.startAll();
    }

    @PostMapping("/stop/{name}")
    @Operation(summary = "Stop a specific process")
    @ApiResponse(responseCode = "200", description = "Process stopped successfully")
    public void stopProcess(@PathVariable String name) {
        processManager.stopProcess(name);
    }

    @PostMapping("/start/{name}")
    @Operation(summary = "Start a specific process")
    @ApiResponse(responseCode = "200", description = "Process started successfully")
    public void startProcess(@PathVariable String name) {
        processManager.startProcess(name);
    }

    @PostMapping("/restart/{name}")
    @Operation(summary = "Restart a specific process")
    @ApiResponse(responseCode = "200", description = "Process started successfully")
    @GetMapping("/restart/{name}")
    public void restartProcess(@PathVariable String name) {
        processManager.restartProcess(name);
    }

    @PostMapping("/details/{name}")
    @Operation(summary = "Return the details for a specific process")
    @ApiResponse(responseCode = "200", description = "Details returned successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcessStatusRest.class)))
    public ProcessStatusRest processesDetails(@PathVariable String name) {
        return new ProcessStatusRest(name, processRepository.getRunningProcess(name));
    }


    @GetMapping("/allProcesses")
    @Operation(summary = "Get all configured processes")
    @ApiResponse(responseCode = "200", description = "List of all processes retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcessStatusRest.class)))
    public Collection<ProcessStatusRest> getAllProcesses() {
        return processRepository.findAllProcessStatusRest();
    }

//    @DeleteMapping("/users/{id}")
//    @Operation(summary = "Delete a user")
//    @ApiResponse(responseCode = "200", description = "User successfully deleted",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = boolean.class)))
//    public boolean deleteUser(@PathVariable int id) {
//        return processRepository.deleteUser(id);
//    }
}