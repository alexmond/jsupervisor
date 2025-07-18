package org.alexmond.supervisor.model;

import lombok.Data;
import org.alexmond.supervisor.config.ProcessConfig;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a user entity in the system.
 * Implements Identifiable interface for consistent ID handling.
 */
@Data
public class ProcessStatusRest {

    private String name;

    /**
     * Current status of the process (e.g., running, stopped, failed)
     */
    private ProcessStatus status;
    /**
     * Process ID assigned by the operating system
     */
    private Long pid;

    /**
     * Timestamp when the process was started
     */
    private LocalDateTime startTime;
    /**
     * Configuration settings for the process
     */
    private LocalDateTime endTime;
    private Integer exitCode;

    private Duration processRuntime;

    private String processUptime;

    private ProcessConfig processConfig;

    public boolean isAlive() {
        return pid != null;
    }


    public ProcessStatusRest(String name,RunningProcess runningProcess) {
        this.name = name;
        this.startTime= runningProcess.getStartTime();
        this.endTime = runningProcess.getEndTime();
        this.exitCode = runningProcess.getExitCode();
        processConfig = runningProcess.getProcessConfig();
        processRuntime = runningProcess.getProcessRuntime();
        processUptime = runningProcess.getProcessRuntimeFormatted();
        if (runningProcess.getProcess() != null) {
            pid = runningProcess.getProcess().pid();
        }
        status = runningProcess.getProcessStatus();
    }
}
