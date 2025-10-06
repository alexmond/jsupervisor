package org.alexmond.jsupervisor.model;

import lombok.Data;
import org.alexmond.jsupervisor.repository.RunningProcess;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class ProcessEvent {

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
    public ProcessEvent(RunningProcess runningProcess, ProcessStatus newStatus) {
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

    public ProcessEvent() {
    }
}
