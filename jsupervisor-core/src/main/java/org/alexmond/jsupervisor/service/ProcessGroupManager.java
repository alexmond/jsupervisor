package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.model.RunningProcess;
import org.apache.commons.io.ThreadUtils;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Manages Group operations for process management, allowing start and stop operations
 * to be performed on multiple processes simultaneously.
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessGroupManager {

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
        orderedStart(processRepository.getProcessOrders());
    }

    /**
     * Asynchronously starts all registered processes that have autoStart configuration enabled.
     * Processes are started according to their defined order with configured delays between starts.
     */
    @Async
    public void autoStartAll() {
        orderedStart(processRepository.getAutostartProcessOrders());
    }

    /**
     * Asynchronously stops all currently running processes.
     *
     */
    @Async
    public void stopAll() {
        // Create a snapshot to avoid ConcurrentModificationException
        Map<String, ?> allProcessesSnapshot = new HashMap<>(processRepository.findAll());
        
        allProcessesSnapshot.entrySet().stream()
                .filter(e -> ((RunningProcess) e.getValue()).isProcessRunning())
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


    @Async
    public void startGroup(String groupName) {
        orderedStart(processRepository.getProcessGroupOrders(groupName));
    }

    @Async
    public void stopGroup(String groupName) {
        var processList = processRepository.getProcessGroups().get(groupName);
        if (processList == null) {
            log.warn("No processes found for group '{}'", groupName);
            return;
        }

        log.info("Stopping processes for group '{}'", groupName);
        List<String> processesCopy = new ArrayList<>(processList);

        processesCopy.forEach(processName -> {
            var runningProcess = processRepository.getRunningProcess(processName);
            if (runningProcess.isProcessRunning()) {
                processManager.stopProcess(processName);
            } else {
                log.info("Process '{}' is not running, skipping stop", processName);
            }
        });
    }

    @Async
    public void restartGroup(String groupName) {
        stopGroup(groupName);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("Thread.sleep interrupted {}", e.getMessage(), e);
        }
        startGroup(groupName);
    }

    private void orderedStart(Map<Integer, List<String>> processOrders) {
        processOrders.forEach((order, processes) -> {
            log.info("Starting for Order {} and {} process(es)", order, processes);
            processes.forEach(processName -> {
                var runningProcess = processRepository.getRunningProcess(processName);
                if (runningProcess.getProcess() == null) {
                    processManager.startProcess(processName);
                } else {
                    log.info("Process '{}' is already running with PID: {}, skipping start",
                            processName, runningProcess.getProcess().pid());
                }
            });
            try {
                ThreadUtils.sleep(config.getAutoStartDelay());
            } catch (InterruptedException e) {
                log.error("Thread.sleep interrupted {}", e.getMessage());
            }
        });
    }
}
