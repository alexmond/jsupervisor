package org.alexmond.supervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ProcessManagerMonitor {
    private final ProcessRepository processRepository;

    @Autowired
    public ProcessManagerMonitor(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }


    @Async
    public CompletableFuture<Void> monitorProcessCompletion(String name, Process proc, LocalDateTime startTime) {
        try {
            int exitCode = proc.waitFor();
            LocalDateTime endTime = LocalDateTime.now();
            processRepository.getRunningProcess(name).setEndTime(endTime);
            processRepository.getRunningProcess(name).setExitCode(exitCode);

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
