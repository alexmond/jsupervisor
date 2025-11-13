package org.alexmond.jsupervisor.model;

/**
 * Represents the possible states of a managed process within the JSupervisor system.
 * This enum defines all valid statuses that a process can have during its lifecycle,
 * from initialization to termination.
 */
public enum ProcessStatus {
    /**
     * Initial state when a process is registered but not yet started
     */
    not_started,
    /**
     * Process is currently executing
     */
    running,
    /**
     * Process has completed successfully with exit code 0
     */
    finished,
    /**
     * Process ended with an unexpected exit code
     */
    unknown,
    /**
     * Process terminated with an error (exit code 1)
     */
    failed,
    /**
     * Process could not be initiated due to configuration or system errors
     */
    failed_to_start,
    /**
     * Process was gracefully terminated (exit code 143)
     */
    stopped,
    /**
     * Process is in the process of stopping
     */
    stopping,
    /**
     * Process was forcefully terminated (exit code 137)
     */
    aborted,
    /**
     * Process is in the process of starting
     */
    starting,
    /**
     * Process is running and responding normally to health checks
     */
    healthy,
    /**
     * Process is running but failing health checks
     */
    unhealthy
}
