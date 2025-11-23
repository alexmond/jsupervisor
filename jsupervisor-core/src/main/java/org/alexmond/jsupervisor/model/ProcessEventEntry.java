package org.alexmond.jsupervisor.model;

//import jakarta.persistence.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents an event entry for process state changes and lifecycle events.
 * This class stores information about process events including status changes,
 * start/end times, and execution details.
 */
@Data
//@Entity
@KeySpace("ProcessEvents")
@NoArgsConstructor
@Schema(description = "Represents an event entry for process state changes and lifecycle events")
public class ProcessEventEntry {

    /**
     * Unique identifier for the process event
     */
    @Id
    @Schema(description = "Unique identifier for the process event", example = "1")
    private Long id;

    /**
     * Process ID of the running process
     */
    @Schema(description = "Process ID of the running process", example = "12345")
    private Long pid;

    /**
     * Timestamp when the event occurred
     */
    @Schema(description = "Timestamp when the event occurred", example = "2025-11-17T10:30:00")
    private LocalDateTime eventTime;

    /**
     * Name of the managed process
     */
    @Schema(description = "Name of the managed process", example = "my-application")
    private String processName;

    /**
     * Timestamp when the process was started
     */
    @Schema(description = "Timestamp when the process was started", example = "2025-11-17T10:00:00")
    private LocalDateTime startTime;

    /**
     * Timestamp when the process ended, null if still running
     */
    @Schema(description = "Timestamp when the process ended, null if still running", example = "2025-11-17T10:15:00")
    private LocalDateTime endTime;

    /**
     * Exit code of the process, null if still running
     */
    @Schema(description = "Exit code of the process, null if still running", example = "0")
    private Integer exitCode;

    /**
     * New status of the process after the event
     */
    @Schema(description = "New status of the process after the event")
    private ProcessStatus newStatus;

    /**
     * Previous status of the process before the event
     */
    @Schema(description = "Previous status of the process before the event")
    private ProcessStatus oldStatus;

    /**
     * Duration for which the process has been running
     */
    @Schema(description = "Duration for which the process has been running", example = "PT15M")
    private Duration processUptime;

    /**
     * Creates a new process event entry based on the running process and its new status.
     *
     * @param runningProcess the process for which the event is being created
     * @param newStatus      the new status that the process is transitioning to
     */
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
