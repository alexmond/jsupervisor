package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * View model for the Events page that represents the data structure used to render the events view.
 * This model extends BaseModel to inherit common page attributes and adds specific fields
 * for displaying process events. It is used to transfer data between the controller and view layer.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EventsPageModel extends BaseModel {
    /**
     * List of process events to be displayed in the events table.
     * Can contain ProcessEvent objects or DTOs representing process lifecycle events.
     * The wildcard type allows flexibility in the type of events that can be displayed.
     */
    private List<?> events;
}
