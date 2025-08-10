package org.alexmond.supervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.ProcessConfig;
import org.alexmond.supervisor.config.SupervisorConfig;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.alexmond.supervisor.repository.RunningProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ProcessManager {
    private final SupervisorConfig supervisorConfig;
    private final ProcessRepository processRepository;
    private final ProcessManagerMonitor processManagerMonitor;
    private final
    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public ProcessManager(SupervisorConfig supervisorConfig, ProcessRepository processRepository,ProcessManagerMonitor processManagerMonitor) {
        this.supervisorConfig = supervisorConfig;
        this.processRepository = processRepository;
        this.processManagerMonitor = processManagerMonitor;
    }

    @Async
    public void restartProcess(String name) {
        stopProcess(name);
        startProcess(name);
    }

    @Async
    public void startProcess(String name){
        var runningProcess = processRepository.getRunningProcess(name);
        ProcessConfig processConfig = runningProcess.getProcessConfig();
        if (processConfig == null) {
            throw new IllegalArgumentException("No process config found for: " + name);
        }

        if (runningProcess.getProcess() != null) {
            log.error("Process {} is already running with pid {}",name,runningProcess.getProcess().pid());
            return;
        }

        runningProcess.reset();

        try {
            List<String> command = new ArrayList<>();
            command.add(processConfig.getCommand());
            command.addAll(processConfig.getArgs());
            
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(processConfig.getWorkingDirectory()));

            // Handle output redirection
            if (processConfig.getRedirectErrorStream()) {
                processBuilder.redirectErrorStream(true);
            } else {
                if(processConfig.getAppendLog())
                    processBuilder.redirectError(
                            ProcessBuilder.Redirect.appendTo(runningProcess.getStderr()));
                else
                    processBuilder.redirectError( runningProcess.getStderr());
            }
            if(processConfig.getAppendLog())
                processBuilder.redirectOutput(
                        ProcessBuilder.Redirect.appendTo(runningProcess.getStdout()));
            else
                processBuilder.redirectOutput( runningProcess.getStdout());

            Map<String, String> environment = processBuilder.environment();
            environment.putAll(processConfig.getEnv());

            // Start the process
            Process proc = processBuilder.start();
            
            // Record start time
            LocalDateTime startTime = LocalDateTime.now();
            runningProcess.setStartTime(startTime);
            
            // Store process references
            runningProcess.setProcess(proc);
            runningProcess.setStartTime(startTime);
            
            log.info("Process '{}' started with PID: {} at {}", name, proc.pid(), startTime);

            if(runningProcess.getHealthCheck() != null) {
                runningProcess.setScheduledFuture(
                        threadPoolTaskScheduler.
                                scheduleAtFixedRate(runningProcess.getHealthCheck(), Duration.ofSeconds(30)));
            }
            
            // Monitor process completion asynchronously using Spring's thread pool
            CompletableFuture<Void> future = processManagerMonitor.monitorProcessCompletion(name, proc, startTime);
            runningProcess.setCompletableFuture(future);
            
        } catch (IOException e) {
            log.error("Failed to start process: {}", name, e);
            runningProcess.setProcess(null);
        }
    }

    @Async
    public void stopProcess(String name) {
        Process process = processRepository.getRunningProcess(name).getProcess();
        if (process != null) {
            log.info("Stopping process: {}", name);
            // Try graceful shutdown first
            if (processRepository.getRunningProcess(name).getScheduledFuture() != null) {
                log.info("Stopping Health check for: {}", name);
                processRepository.getRunningProcess(name).getScheduledFuture().cancel(true);
            }
            process.destroy();
            try {
                boolean exited = process.waitFor( processRepository.getRunningProcess(name).getProcessConfig().getShutdownTimeout().toSeconds(), java.util.concurrent.TimeUnit.SECONDS);
                if (!exited) {
                    log.warn("Process {} did not exit gracefully, force killing...", name);
                    process.destroyForcibly();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for process to stop: {}", name);
            }

            // Clean up
            processRepository.getRunningProcess(name).setProcess(null);
            processRepository.getRunningProcess(name).setCompletableFuture(null);
        }
    }



}
