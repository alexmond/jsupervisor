package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Base model class that provides common attributes for UI model objects.
 * Used as a parent class for specific UI model implementations.
 */
@Data
@SuperBuilder
@lombok.NoArgsConstructor
public class BaseModel {
    /**
     * The title displayed in the UI, e.g., "Process Details"
     */
    private String title;                 // "Process Details"

    /**
     * The currently active page identifier, e.g., "processes"
     */
    private String activePage;            // "processes"

    private String content;
}
