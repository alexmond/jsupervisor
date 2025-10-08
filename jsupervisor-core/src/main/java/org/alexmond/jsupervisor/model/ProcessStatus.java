package org.alexmond.jsupervisor.model;

/**
 * Represents the possible states of a managed process within the JSupervisor system.
 * This enum defines all valid statuses that a process can have during its lifecycle,
 * from initialization to termination.
 */
public enum ProcessStatus {
    not_started,
    running,
    finished,
    unknown,
    failed,
    failed_to_start,
    stopped,
    stopping,
    aborted,
    starting,
    healthy,
    unhealthy
}
