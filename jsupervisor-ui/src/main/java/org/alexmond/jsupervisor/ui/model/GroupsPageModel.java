package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;

import java.util.Collection;
import java.util.Map;

/**
 * Model class representing a page displaying process groups in the UI.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GroupsPageModel extends BaseModel {
    /**
     * Map of group names to their respective processes.
     */
    private Map<String, Collection<ProcessStatusInfo>> groups;
}
