package org.alexmond.jsupervisor.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.healthcheck.HealthCheck;
import org.alexmond.jsupervisor.healthcheck.HealthCheckFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

/**
 * Represents a running process managed by the JSupervisor system.
 * This class maintains the state and metadata of a supervised process,
 * including its runtime information, health checks, and status monitoring.
 * It handles process lifecycle events, logging, and health monitoring while
 * providing thread-safe access to process state through synchronized methods.
 */
@Getter
@Setter
@Slf4j
public class RunningProcess {

    /**
     * Publisher for process-related events
     */
    private final ApplicationEventPublisher eventPublisher;
    /**
     * Unique identifier for the process
     */
    private final String processName;
    /**
     * Configuration settings for the process
     */
    @Delegate
    private final ProcessConfig processConfig;

    /**
     * Self-reference for the running process
     */
    private RunningProcess runningProcess;
    /**
     * Health check implementation for the process
     */
    private HealthCheck healthCheck;
    /**
     * Java Process instance
     */
    private Process process = null;
    /**
     * Timestamp when the process started
     */
    private LocalDateTime startTime;
    /**
     * Timestamp when the process ended
     */
    private LocalDateTime endTime;
    /**
     * Process exit code
     */
    private Integer exitCode;
    /**
     * Standard output log file
     */
    private File stdout;
    /**
     * Standard error log file
     */
    private File stderr = null;
    /**
     * Application-specific log file
     */
    private File application = null;
    /**
     * Future representing the asynchronous process completion
     */
    private CompletableFuture<Void> completableFuture = null;
    /**
     * Future for scheduled tasks related to the process
     */
    private ScheduledFuture<?> scheduledFuture = null;
    /**
     * Path to the stdout log file
     */
    private String stdoutLogfile;
    /**
     * Path to the stderr log file
     */
    private String stderrLogfile;
    /**
     * Current status of the process
     */
    private ProcessStatus processStatus = ProcessStatus.not_started;
    /**
     * Error log message in case of failure
     */
    private String failedErrorLog;

    /**
     * Constructs a new RunningProcess instance.
     *
     * @param processName    unique identifier for the process
     * @param processConfig  configuration settings for the process
     * @param eventPublisher publisher for process-related events
     */
    public RunningProcess(String processName, ProcessConfig processConfig, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.processName = processName;
        this.processConfig = processConfig;

        if (!processConfig.isRedirectErrorStream()) {
            if (processConfig.getStderrLogfile() != null) {
                stderrLogfile = processConfig.getStderrLogfile();
            } else {
                stderrLogfile = processName + "-stderr.log";
            }
            stderr = new File(stderrLogfile);
        }

        if (processConfig.getStdoutLogfile() != null) {
            stdoutLogfile = processConfig.getStdoutLogfile();
        } else {
            stdoutLogfile = processName + "-stdout.log";
        }

        stdout = new File(stdoutLogfile);
        if (processConfig.getApplicationLog() != null) {
            application = new File(processConfig.getApplicationLog());
        }
        healthCheck = HealthCheckFactory.getHealthCheck(processConfig, this);
    }

    /**
     * Sets the Java Process instance in a thread-safe manner.
     *
     * @param process the Java Process to set
     */
    @Synchronized
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     * Sets the process start time in a thread-safe manner.
     *
     * @param startTime the start time to set
     */
    @Synchronized
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the process end time in a thread-safe manner.
     *
     * @param endTime the end time to set
     */
    @Synchronized
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the process exit code in a thread-safe manner.
     *
     * @param exitCode the exit code to set
     */
    @Synchronized
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * Sets the process status and publishes a status change event in a thread-safe manner.
     *
     * @param processStatus the new process status to set
     */
    @Synchronized
    public void setProcessStatus(ProcessStatus processStatus) {
        if (this.processStatus != processStatus) {
            log.info("setProcessStatus {}", processStatus);
            eventPublisher.publishEvent(new ProcessEvent(new ProcessEventEntry(this, processStatus)));

            this.processStatus = processStatus;
        }
    }

    /**
     * Sets the completable future for process completion in a thread-safe manner.
     *
     * @param completableFuture the completable future to set
     */
    @Synchronized
    public void setCompletableFuture(CompletableFuture<Void> completableFuture) {
        this.completableFuture = completableFuture;
    }

    /**
     * Sets the scheduled future for process-related tasks in a thread-safe manner.
     *
     * @param scheduledFuture the scheduled future to set
     */
    @Synchronized
    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    /**
     * Checks if the process is currently running.
     *
     * @return true if the process is alive, false otherwise
     */
    public boolean isProcessRunning() {
        return process != null && process.isAlive();
    }

    /**
     * Resets the process timing information.
     * Clears start time, end time, and exit code.
     */
    @Synchronized
    public void reset() {
        startTime = null;
        endTime = null;
        exitCode = null;
    }


    /**
     * Calculates the total runtime of the process.
     *
     * @return Duration representing the time the process has been/was running
     */
    public Duration getProcessRuntime() {
        if (startTime == null) {
            return Duration.ZERO;
        }

        if (endTime != null) {
            // Process has ended
            return Duration.between(startTime, endTime);
        } else if (isProcessRunning()) {
            // Process is still running
            return Duration.between(startTime, LocalDateTime.now());
        } else {
            // Process ended but no end time recorded (shouldn't happen normally)
            return Duration.between(startTime, LocalDateTime.now());
        }
    }


    /**
     * Formats the process runtime into a human-readable string.
     *
     * @return formatted string representation of the process runtime (e.g., "HH:mm:ss" or "mm:ss")
     */
    public String getProcessRuntimeFormatted() {
        Duration runtime = getProcessRuntime();
        if (runtime == Duration.ZERO) return "";

        long hours = runtime.toHours();
        long minutes = runtime.toMinutesPart();
        long seconds = runtime.toSecondsPart();
        long millis = runtime.toMillisPart();

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.format("%02d seconds", seconds);
        }
    }
}