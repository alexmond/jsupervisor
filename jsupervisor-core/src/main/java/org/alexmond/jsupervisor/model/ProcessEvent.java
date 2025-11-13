package org.alexmond.jsupervisor.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Represents an event that occurs during process lifecycle management.
 * This class extends Spring's ApplicationEvent to provide process-specific event handling.
 * It captures various attributes and state changes of a managed process through
 * the {@link ProcessEventEntry} object.
 *
 * @see ApplicationEvent
 * @see ProcessEventEntry
 */
@Setter
@Getter
public class ProcessEvent extends ApplicationEvent {
    /**
     * The event entry containing detailed information about the process event.
     */
    private ProcessEventEntry entry;

    /**
     * Constructs a new ProcessEvent with the specified event entry.
     *
     * @param entry the ProcessEventEntry containing the event details
     */
    public ProcessEvent(ProcessEventEntry entry) {
        super(entry);
        this.entry = entry;
    }

}
