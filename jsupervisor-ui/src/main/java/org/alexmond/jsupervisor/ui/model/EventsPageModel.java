package org.alexmond.jsupervisor.ui.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.repository.EventRepository;

import java.util.List;

/**
 * View model for the Events page.
 * Holds page metadata and the list of events to render.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EventsPageModel extends BaseModel {
    private List<?> events;     // List of event DTOs/entities shown in the table
}
