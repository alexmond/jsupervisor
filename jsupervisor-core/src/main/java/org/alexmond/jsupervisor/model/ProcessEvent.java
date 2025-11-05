package org.alexmond.jsupervisor.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Represents an event that occurs during process lifecycle management.
 * This class captures various attributes and state changes of a managed process.
 */
@Setter
@Getter
public class ProcessEvent extends ApplicationEvent {
    private ProcessEventEntry entry;

    public ProcessEvent(ProcessEventEntry entry) {
        super(entry);
        this.entry = entry;
    }

    public ProcessEvent(ProcessEventEntry entry, Clock clock) {
        super(entry, clock);
        this.entry = entry;
    }
}
