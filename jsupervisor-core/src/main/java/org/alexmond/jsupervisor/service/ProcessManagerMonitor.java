package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Monitors and manages the lifecycle of running processes.
 * This component tracks process execution, updates process status,
 * and handles process completion events.
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessManagerMonitor {
    private final ProcessRepository processRepository;
    private final EventRepository eventRepository;


    /**
     * Asynchronously monitors a process until completion and updates its status.
     * Upon process completion, updates the process status based on exit code and performs cleanup.
     *
     * @param name      the name of the process to monitor
     * @param proc      the Java Process object to monitor
     * @param startTime the time when the process started
     * @return CompletableFuture<Void> representing the completion of monitoring
     */
    @Async
    public CompletableFuture<Void> monitorProcessCompletion(String name, Process proc, LocalDateTime startTime) {
        try {
            int exitCode = proc.waitFor();
            LocalDateTime endTime = LocalDateTime.now();
            processRepository.getRunningProcess(name).setEndTime(endTime);
            processRepository.getRunningProcess(name).setExitCode(exitCode);
            ProcessStatus newStatus;
            switch (exitCode) {
                case 0 -> newStatus = ProcessStatus.finished;
                case 1 -> newStatus = ProcessStatus.failed;
                case 143 -> newStatus = ProcessStatus.stopped;
                case 137 -> newStatus = ProcessStatus.aborted;
                default -> newStatus = ProcessStatus.unknown;
            }
            ;
            processRepository.getRunningProcess(name).setProcessStatus(newStatus);
            log.info("Process '{}' ended with exit code: {} after running for: {}",
                    name, exitCode, processRepository.getRunningProcess(name).getProcessRuntimeFormatted());

            // Clean up
            processRepository.getRunningProcess(name).setProcess(null);
            processRepository.getRunningProcess(name).setCompletableFuture(null);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Process monitoring interrupted for: {}", name);
        }

        return CompletableFuture.completedFuture(null);
    }

}
