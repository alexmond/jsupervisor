package org.alexmond.supervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ProcessManagerBulk {
    private final Map<String, CompletableFuture<Void>> processFutures = new HashMap<>();
    private final ProcessRepository processRepository;
    private final ProcessManager processManager;

    @Autowired
    public ProcessManagerBulk(ProcessRepository processRepository,ProcessManager processManager) {
        this.processRepository = processRepository;
        this.processManager = processManager;
    }

    @Async
    public void startAll() throws IOException {
        processRepository.findAll().entrySet().stream()
                .filter(e -> !e.getValue().isProcessRunning())
                .forEach(e -> processManager.startProcess(e.getKey()));
    }
    @Async
    public void stopAll() throws IOException {
        processRepository.findAll().entrySet().stream()
                .filter(e -> e.getValue().isProcessRunning())
                .forEach(e -> processManager.stopProcess(e.getKey()));
    }


}
