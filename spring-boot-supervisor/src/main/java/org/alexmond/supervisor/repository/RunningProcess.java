package org.alexmond.supervisor.repository;

import lombok.Data;
import org.alexmond.supervisor.config.ProcessConfig;
import org.alexmond.supervisor.healthcheck.ActuatorHealthCheck;
import org.alexmond.supervisor.healthcheck.HealthCheck;
import org.alexmond.supervisor.healthcheck.HealthCheckFactory;
import org.alexmond.supervisor.model.ProcessStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

/**
 * Represents a user entity in the system.
 * Implements Identifiable interface for consistent ID handling.
 */

@Data
public class RunningProcess {

    private ProcessConfig processConfig;
    private String processName;
    private HealthCheck healthCheck;
    private Process process = null;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer exitCode;
    private File stdout;
    private File stderr = null;
    private CompletableFuture<Void> completableFuture = null;
    private ScheduledFuture<?> scheduledFuture = null;
    private String stdoutLogfile;
    private String stderrLogfile;

    public RunningProcess(String processName,ProcessConfig processConfig) {
        this.processName = processName;
        this.processConfig = processConfig;

        if (!processConfig.getRedirectErrorStream()) {
            if (processConfig.getStderrLogfile() != null) {
                stderrLogfile = processConfig.getStderrLogfile();
            }else{
                stderrLogfile = processName+"-stderr.log";
            }
            stderr =  new File(stderrLogfile);
        }

        if (processConfig.getStdoutLogfile() != null) {
            stdoutLogfile = processConfig.getStdoutLogfile();
        } else {
            stdoutLogfile = processName + "-stdout.log";
        }

        stdout = new File(processConfig.getStdoutLogfile());
        healthCheck = HealthCheckFactory.getHealthCheck(processConfig);
    }

    public boolean isProcessRunning() {
        return process != null && process.isAlive();
    }

    public void reset(){
        startTime = null;
        endTime = null;
        exitCode = null;
    };

    public ProcessStatus getProcessStatus() {
        if (process == null && startTime == null) {
            return ProcessStatus.not_started;
        } else if (process != null && process.isAlive()) {
            return ProcessStatus.running;
        } else if (endTime != null) {
            return switch (exitCode) {
                case 0 -> ProcessStatus.finished;
                case 1 -> ProcessStatus.failed;
                case 143 -> ProcessStatus.stopped;
                case 137 -> ProcessStatus.aborted;
                default -> ProcessStatus.unknown;
            };
        } else {
            return ProcessStatus.unknown;
        }
    }

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


    public String getProcessRuntimeFormatted() {
        Duration runtime = getProcessRuntime();
        if(runtime == Duration.ZERO) return "";

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