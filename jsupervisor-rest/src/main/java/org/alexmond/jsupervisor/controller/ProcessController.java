package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.controller.model.ResponseMessage;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of processes")
    })
    public Collection<ProcessStatusRest> getAllProcesses() {
        return processRepository.findAllProcessStatusRest();
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get process details", description = "Retrieve detailed information about a specific process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved process details"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ProcessStatusRest getProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        return processRepository.getRunningProcessRest(name);
    }

    @PostMapping("/start/{name}")
    @Operation(summary = "Start process", description = "Start a specific process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process start initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid process configuration"),
            @ApiResponse(responseCode = "500", description = "Failed to start process")
    })
    public ResponseEntity<ResponseMessage> startProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        try {
            processManager.startProcess(name);
            ResponseMessage resp = new ResponseMessage();
            resp.setMessage("Process start initiated: " + name);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            ResponseMessage resp = new ResponseMessage();
            resp.setError("Invalid process configuration");
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(400).body(resp);
        } catch (Exception e) {
            ResponseMessage resp = new ResponseMessage();
            resp.setError("Failed to start process");
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    @PostMapping("/stop/{name}")
    @Operation(summary = "Stop process", description = "Stop a specific process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process stop initiated successfully"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "500", description = "Failed to stop process")
    })
    public ResponseEntity<ResponseMessage> stopProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        try {
            ProcessStatusRest process = processRepository.getRunningProcessRest(name);
            if (process == null) {
                ResponseMessage resp = new ResponseMessage();
                resp.setError("Process not found");
                resp.setMessage(name);
                return ResponseEntity.status(404).body(resp);
            }
            processManager.stopProcess(name);
            ResponseMessage resp = new ResponseMessage();
            resp.setMessage("Process stop initiated: " + name);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            ResponseMessage resp = new ResponseMessage();
            resp.setError("Failed to stop process");
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    @PostMapping("/restart/{name}")
    @Operation(summary = "Restart process", description = "Restart a specific process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process restart initiated successfully"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "500", description = "Failed to restart process")
    })
    public ResponseEntity<ResponseMessage> restartProcess(
            @Parameter(description = "Process name") @PathVariable String name) {
        try {
            ProcessStatusRest process = processRepository.getRunningProcessRest(name);
            if (process == null) {
                ResponseMessage resp = new ResponseMessage();
                resp.setError("Process not found");
                resp.setMessage(name);
                return ResponseEntity.status(404).body(resp);
            }
            processManager.restartProcess(name);
            ResponseMessage resp = new ResponseMessage();
            resp.setMessage("Process restart initiated: " + name);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            ResponseMessage resp = new ResponseMessage();
            resp.setError("Failed to restart process");
            resp.setMessage(e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    @GetMapping("/status/{name}")
    @Operation(summary = "Get process status", description = "Get the current status of a specific process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved process status"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve process status")
    })
    public ProcessStatusRest getProcessStatus(
            @Parameter(description = "Process name") @PathVariable String name) {
        return processRepository.getRunningProcessRest(name);
    }
}
