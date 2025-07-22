package org.alexmond.supervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.alexmond.supervisor.config.SupervisorConfig;
import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.model.RunningProcess;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.alexmond.supervisor.service.ProcessManager;
import org.alexmond.supervisor.service.ProcessManagerBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    public RestProcessController(ProcessRepository processRepository, SupervisorConfig supervisorConfig , ProcessManager processManager, ProcessManagerBulk processManagerBulk) {
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

    @PostMapping("/details/{name}")
    @Operation(summary = "Return the details for a specific process")
    @ApiResponse(responseCode = "200", description = "Details returned successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProcessStatusRest.class)))
    public ProcessStatusRest processesDetails(@PathVariable String name) {
        return new ProcessStatusRest(name, processRepository.getRunningProcess(name));
    }


    @GetMapping("/users")
    @Operation(summary = "Returns all users", tags = {"User"})
    @ApiResponse(responseCode = "200", description = "Returns all users",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RunningProcess.class)))
    public Collection<ProcessStatusRest> getAll() {
        return processRepository.findAllProcessStatusRest();
    }

//    @PostMapping("/users")
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Register a new user")
//    @ApiResponse(responseCode = "201", description = "User successfully created",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = Process.class)))
//    public Process register(@RequestParam(defaultValue = "Stranger") String name) {
//        Process newProcess = new Process(counter.incrementAndGet(), name);
//        return processRepository.addUser(newProcess);
//    }

//    @PutMapping("/users/{id}")
//    @Operation(summary = "Update a user's name")
//    @ApiResponse(responseCode = "200", description = "User successfully updated",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = Process.class)))
//    public Process updateUser(@PathVariable int id, @RequestParam String newName) {
//        return processRepository.updateUser(id, newName)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
//    }
//
//    @DeleteMapping("/users/{id}")
//    @Operation(summary = "Delete a user")
//    @ApiResponse(responseCode = "200", description = "User successfully deleted",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = boolean.class)))
//    public boolean deleteUser(@PathVariable int id) {
//        return processRepository.deleteUser(id);
//    }
}