package org.alexmond.supervisor.model;

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
