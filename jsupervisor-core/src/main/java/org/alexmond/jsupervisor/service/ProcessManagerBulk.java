package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
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

    SupervisorConfig config;
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
     */
    @Async
    public void startAll() {
        processRepository.getProcessOrders().forEach((key, value) -> {
            log.info("Starting for Order {} and {} process(es)", key, value);
            value.forEach(processManager::startProcess);
            try {
                Thread.sleep(config.getAutoStartDelay().toMillis());
            } catch (InterruptedException e) {
                log.error("Thread.sleep interrupted {}",e.getMessage(), e);
            }
        });
    }

    @Async
    public void autoStartAll() {
        processRepository.getProcessOrders().forEach((key, value) -> {
            log.info("Auto-start initiated for Order {} and {} process(es)", key, value);
            value.forEach(process -> {
                if (processRepository.getRunningProcess(process).getProcessConfig().isAutoStart()) {
                    processManager.startProcess(process);
                }
            });
            try {
                Thread.sleep(config.getAutoStartDelay().toMillis());
            } catch (InterruptedException e) {
                log.error("Thread.sleep interrupted {}",e.getMessage(), e);
            }
        });
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
