package org.alexmond.jsupervisor.repository;

import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.healthcheck.HealthCheck;
import org.alexmond.jsupervisor.healthcheck.HealthCheckFactory;
import org.alexmond.jsupervisor.model.ProcessEvent;
import org.alexmond.jsupervisor.model.ProcessStatus;

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
@Slf4j
public class RunningProcess {

    private RunningProcess runningProcess;
    private EventRepository eventRepository;
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
    private ProcessStatus processStatus = ProcessStatus.not_started;

    public RunningProcess(String processName, ProcessConfig processConfig, EventRepository eventRepository) {
        this.processName = processName;
        this.processConfig = processConfig;
        this.eventRepository = eventRepository;

        if (!processConfig.getRedirectErrorStream()) {
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

        stdout = new File(processConfig.getStdoutLogfile());
        healthCheck = HealthCheckFactory.getHealthCheck(processConfig, this);
    }

    @Synchronized
    public void setProcess(Process process) {
        this.process = process;
    }

    @Synchronized
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Synchronized
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Synchronized
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    @Synchronized
    public void setProcessStatus(ProcessStatus processStatus) {
        log.info("setProcessStatus {}", processStatus);
        eventRepository.save(new ProcessEvent(this, processStatus));
        this.processStatus = processStatus;
    }

    @Synchronized
    public void setCompletableFuture(CompletableFuture<Void> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Synchronized
    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public boolean isProcessRunning() {
        return process != null && process.isAlive();
    }

    @Synchronized
    public void reset() {
        startTime = null;
        endTime = null;
        exitCode = null;
    }

    ;


//    public ProcessStatus getProcessStatus() {
//        if (process == null && startTime == null) {
//            return ProcessStatus.not_started;
//        } else if (process != null && process.isAlive()) {
//            return ProcessStatus.running;
//        } else if (endTime != null) {
//            return switch (exitCode) {
//                case 0 -> ProcessStatus.finished;
//                case 1 -> ProcessStatus.failed;
//                case 143 -> ProcessStatus.stopped;
//                case 137 -> ProcessStatus.aborted;
//                default -> ProcessStatus.unknown;
//            };
//        } else {
//            return ProcessStatus.unknown;
//        }
//    }

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