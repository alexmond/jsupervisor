package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.springframework.scheduling.annotation.Async;

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
    //MultiProcessManager
    //ProcessGroupManager

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
     * Configuration settings for the supervisor, including startup delays and other parameters.
     */
    private final SupervisorConfig config;

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
                log.error("Thread.sleep interrupted {}", e.getMessage());
            }
        });
    }

    /**
     * Asynchronously starts all registered processes that have autoStart configuration enabled.
     * Processes are started according to their defined order with configured delays between starts.
     */
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
                log.error("Thread.sleep interrupted {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Asynchronously stops all currently running processes.
     *
     */
    @Async
    public void stopAll() {
        processRepository.findAll().entrySet().stream()
                .filter(e -> e.getValue().isProcessRunning())
                .forEach(e -> processManager.stopProcess(e.getKey()));
    }

    /**
     * Asynchronously restarts all processes by stopping all running processes first,
     * waiting for a brief period, and then starting them again.
     */
    @Async
    public void restartAll() {
        stopAll();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("Thread.sleep interrupted {}", e.getMessage(), e);
        }
        startAll();
    }


}
