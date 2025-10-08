package org.alexmond.jsupervisor.ui.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BaseModel {
    private String title;                 // "Process Details"
    private String activePage;            // "processes"
}
