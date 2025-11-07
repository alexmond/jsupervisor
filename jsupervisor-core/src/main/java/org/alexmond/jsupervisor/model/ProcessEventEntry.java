package org.alexmond.jsupervisor.model;

//import jakarta.persistence.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
//@Entity
@KeySpace("ProcessEvents")
@NoArgsConstructor
public class ProcessEventEntry {

    /**
     * Unique identifier for the process event
     */
    @Id
    private Long id;

    /**
     * Process ID of the running process
     */
    private Long pid;

    /**
     * Timestamp when the event occurred
     */
    private LocalDateTime eventTime;

    /**
     * Name of the managed process
     */
    private String processName;

    /**
     * Timestamp when the process was started
     */
    private LocalDateTime startTime;

    /**
     * Timestamp when the process ended, null if still running
     */
    private LocalDateTime endTime;

    /**
     * Exit code of the process, null if still running
     */
    private Integer exitCode;

    /**
     * New status of the process after the event
     */
    private ProcessStatus newStatus;

    /**
     * Previous status of the process before the event
     */
    private ProcessStatus oldStatus;

    /**
     * Duration for which the process has been running
     */
    private Duration processUptime;

    public ProcessEventEntry(RunningProcess runningProcess, ProcessStatus newStatus) {

        if (runningProcess.getProcess() != null) {
            this.pid = runningProcess.getProcess().pid();
        }
        this.processName = runningProcess.getProcessName();
        this.startTime = runningProcess.getStartTime();
        this.endTime = runningProcess.getEndTime();
        this.exitCode = runningProcess.getExitCode();
        this.oldStatus = runningProcess.getProcessStatus();
        this.newStatus = newStatus;

        this.eventTime = LocalDateTime.now();
        if (this.startTime == null) {
            this.processUptime = Duration.ZERO;
        } else if (this.endTime != null) {
            // Process has ended
            this.processUptime = Duration.between(this.startTime, this.endTime);
        } else {
            // Process is still running or no end time recorded
            this.processUptime = Duration.between(this.startTime, this.eventTime);
        }
    }
}
