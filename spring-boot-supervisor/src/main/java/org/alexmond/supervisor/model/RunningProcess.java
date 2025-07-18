package org.alexmond.supervisor.model;

import lombok.Data;
import lombok.experimental.Delegate;
import org.alexmond.supervisor.config.ProcessConfig;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a user entity in the system.
 * Implements Identifiable interface for consistent ID handling.
 */
@Data
public class RunningProcess {

    @Delegate private ProcessConfig processConfig;
    private Process process = null;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer exitCode;
    private File stdout;
    private File stderr = null;

    public RunningProcess(ProcessConfig processConfig) {
        this.processConfig = processConfig;
        if (!processConfig.getRedirectErrorStream()) {
            stderr =  new File(processConfig.getStderrLogfile());
        }
        stdout = new File(processConfig.getStdoutLogfile());
    }

    private CompletableFuture<Void> completableFuture = null;

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
            return ProcessStatus.NOT_STARTED;
        } else if (process != null && process.isAlive()) {
            return ProcessStatus.RUNNING;
        } else if (endTime != null) {
            return switch (exitCode) {
                case 0 -> ProcessStatus.FINISHED;
                case 1 -> ProcessStatus.FAILED;
                case 143 -> ProcessStatus.STOPPED;
                case 137 -> ProcessStatus.ABORTED;
                default -> ProcessStatus.UNKNOWN;
            };
        } else {
            return ProcessStatus.UNKNOWN;
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