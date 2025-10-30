package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Manages bulk operations for process management, allowing start and stop operations
 * to be performed on multiple processes simultaneously.
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessManagerBulk {
    /**
     * Stores CompletableFuture objects for tracking asynchronous process operations.
     */
    private final Map<String, CompletableFuture<Void>> processFutures = new HashMap<>();

    /**
     * Repository for managing process-related data and operations.
     */
    private final ProcessRepository processRepository;

    /**
     * Manager for handling individual process operations.
     */
    private final ProcessManager processManager;


    /**
     * Asynchronously starts all registered processes that are not currently running.
     *
     * @throws IOException if an I/O error occurs while starting processes
     */
    @Async
    public void startAll() throws IOException {
        processRepository.findAll().entrySet().stream()
                .filter(e -> !e.getValue().isProcessRunning())
                .forEach(e -> processManager.startProcess(e.getKey()));
    }

    /**
     * Asynchronously stops all currently running processes.
     *
     * @throws IOException if an I/O error occurs while stopping processes
     */
    @Async
    public void stopAll() throws IOException {
        processRepository.findAll().entrySet().stream()
                .filter(e -> e.getValue().isProcessRunning())
                .forEach(e -> processManager.stopProcess(e.getKey()));
    }


}
