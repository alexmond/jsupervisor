package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusRest;

/**
 * Model class for displaying detailed process information in the UI.
 * Extends BaseModel to incorporate common model attributes while providing
 * specific process status data for the detail view.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProcessDetailPageModel extends BaseModel {
    /**
     * Process status information containing details about the managed process,
     * including its current state and runtime information.
     */
    private ProcessStatusRest proc;
}
