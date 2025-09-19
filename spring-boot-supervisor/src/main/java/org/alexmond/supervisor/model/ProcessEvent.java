package org.alexmond.supervisor.model;

import lombok.Data;
import org.alexmond.supervisor.config.ProcessConfig;
import org.alexmond.supervisor.healthcheck.HealthCheck;
import org.alexmond.supervisor.repository.RunningProcess;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

@Data
public class ProcessEvent {

    public ProcessEvent(RunningProcess runningProcess,ProcessStatus newStatus) {
        this.eventTime = LocalDateTime.now();
        if (runningProcess.getProcess() != null) {
            this.pid = runningProcess.getProcess().pid();
        }
        this.processName = runningProcess.getProcessName();
        this.startTime = runningProcess.getStartTime();
        this.endTime = runningProcess.getEndTime();
        this.exitCode = runningProcess.getExitCode();
        this.oldStatus = runningProcess.getProcessStatus();
        this.newStatus = newStatus;
    }

    private Long id;
    private Long pid;
    private LocalDateTime eventTime;
    private String processName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer exitCode;
    private ProcessStatus newStatus;
    private ProcessStatus oldStatus;
    private Duration processUptime;

    public ProcessEvent() {
    }
}
