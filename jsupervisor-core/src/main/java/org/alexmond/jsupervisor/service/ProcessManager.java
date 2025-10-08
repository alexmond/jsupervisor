package org.alexmond.jsupervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Manages the lifecycle of processes in the supervisor system.
 * Responsible for starting, stopping, and restarting processes according to their configuration.
 * Handles process monitoring, output redirection, and cleanup operations.
 */
@Component
@Slf4j
public class ProcessManager {
    private final SupervisorConfig supervisorConfig;
    private final ProcessRepository processRepository;
    private final ProcessManagerMonitor processManagerMonitor;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final EventRepository eventRepository;

    /**
     * Constructs a ProcessManager with required dependencies.
     *
     * @param supervisorConfig        Configuration for the supervisor system
     * @param processRepository       Repository for managing process states
     * @param processManagerMonitor   Monitor for tracking process completion
     * @param threadPoolTaskScheduler Scheduler for async operations
     * @param eventRepository         Repository for storing process events
     */
    @Autowired
    public ProcessManager(SupervisorConfig supervisorConfig, ProcessRepository processRepository, ProcessManagerMonitor processManagerMonitor, ThreadPoolTaskScheduler threadPoolTaskScheduler, EventRepository eventRepository) {
        this.supervisorConfig = supervisorConfig;
        this.processRepository = processRepository;
        this.processManagerMonitor = processManagerMonitor;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.eventRepository = eventRepository;
    }

    /**
     * Restarts a process by stopping it and then starting it again.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to restart
     */
    @Async
    public void restartProcess(String name) {
        stopProcess(name);
        startProcess(name);
    }

    /**
     * Starts a process with the given name according to its configuration.
     * Handles process creation, output redirection, environment setup, and monitoring.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to start
     * @throws IllegalArgumentException if no process configuration is found for the given name
     */
    @Async
    public void startProcess(String name) {
        var runningProcess = processRepository.getRunningProcess(name);
        ProcessConfig processConfig = runningProcess.getProcessConfig();
        if (processConfig == null) {
            throw new IllegalArgumentException("No process config found for: " + name);
        }

        if (runningProcess.getProcess() != null) {
            log.error("Process {} is already running with pid {}", name, runningProcess.getProcess().pid());
            return;
        }
        runningProcess.reset();
        runningProcess.setProcessStatus(ProcessStatus.starting);

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
                if (processConfig.getAppendLog())
                    processBuilder.redirectError(
                            ProcessBuilder.Redirect.appendTo(runningProcess.getStderr()));
                else
                    processBuilder.redirectError(runningProcess.getStderr());
            }
            if (processConfig.getAppendLog())
                processBuilder.redirectOutput(
                        ProcessBuilder.Redirect.appendTo(runningProcess.getStdout()));
            else
                processBuilder.redirectOutput(runningProcess.getStdout());

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
            runningProcess.setProcessStatus(ProcessStatus.running);

            log.info("Process '{}' started with PID: {} at {}", name, proc.pid(), startTime);

            if (runningProcess.getHealthCheck() != null) {
                runningProcess.setScheduledFuture(
                        threadPoolTaskScheduler.
                                scheduleAtFixedRate(runningProcess.getHealthCheck(), Duration.ofSeconds(30)));
            }

            // Monitor process completion asynchronously using Spring's thread pool
            CompletableFuture<Void> future = processManagerMonitor.monitorProcessCompletion(name, proc, startTime);
            runningProcess.setCompletableFuture(future);

        } catch (IOException e) {
            log.error("Failed to start process: {}", name, e);
            runningProcess.setProcessStatus(ProcessStatus.failed);
            runningProcess.setFailedErrorLog(e.getMessage());
            runningProcess.setProcess(null);
        }
    }

    /**
     * Stops a running process with the given name.
     * Attempts graceful shutdown first, followed by force kill if necessary.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to stop
     */
    @Async
    public void stopProcess(String name) {
        RunningProcess runningProcess = processRepository.getRunningProcess(name);
        Process process = runningProcess.getProcess();
        if (process != null) {
            log.info("Stopping process: {}", name);
            runningProcess.setProcessStatus(ProcessStatus.stopping);
            // Try graceful shutdown first
            if (runningProcess.getScheduledFuture() != null) {
                log.info("Stopping Health check for: {}", name);
                runningProcess.getScheduledFuture().cancel(true);
            }
            process.destroy();
            try {
                boolean exited = process.waitFor(runningProcess.getProcessConfig().getShutdownTimeout().toSeconds(), java.util.concurrent.TimeUnit.SECONDS);
                if (!exited) {
                    log.warn("Process {} did not exit gracefully, force killing...", name);
                    process.destroyForcibly();
                    runningProcess.setProcessStatus(ProcessStatus.aborted);
                } else {
                    runningProcess.setProcessStatus(ProcessStatus.stopped);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for process to stop: {}", name);
                runningProcess.setProcessStatus(ProcessStatus.unknown);
            }

            // Clean up
            runningProcess.setProcess(null);
            runningProcess.setCompletableFuture(null);
        }
    }


}
