package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;

import java.util.Collection;

/**
 * Model class representing a page displaying process information in the UI.
 * Extends BaseModel to provide common functionality for UI model classes.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProcessesPageModel extends BaseModel {
    /**
     * Collection of process statuses representing the current state of all managed processes.
     */
    private Collection<ProcessStatusInfo> processes;
}
