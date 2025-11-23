package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * View model for the Events page that represents the data structure used to render the events view.
 * This model extends BaseModel to inherit common page attributes and adds specific fields
 * for displaying process events with pagination support.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EventsPageModel extends BaseModel {

    /**
     * Paginated list of process events to be displayed in the events table.
     */
    private Page<ProcessEventEntry> eventsPage;

    /**
     * Current page number (0-based).
     */
    private int currentPage;

    /**
     * Total number of pages.
     */
    private int totalPages;

    /**
     * Total number of events across all pages.
     */
    private long totalElements;

    /**
     * Number of items per page.
     */
    private int pageSize;

    /**
     * Field being used for sorting.
     */
    private String sortBy;

    /**
     * Sort direction (asc or desc).
     */
    private String sortDirection;

    /**
     * List of page numbers to display in pagination controls.
     * Pre-calculated to avoid SpEL Math operations in templates.
     */
    private List<Integer> pageNumbers;

    /**
     * Currently selected process name filter (null or empty means "All Processes").
     */
    private String filterProcessName;

    /**
     * List of all available process names for the dropdown filter.
     */
    private List<String> availableProcessNames;
}
