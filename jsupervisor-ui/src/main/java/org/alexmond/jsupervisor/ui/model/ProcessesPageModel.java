package org.alexmond.jsupervisor.ui.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusRest;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProcessesPageModel extends BaseModel {
  private Collection<ProcessStatusRest> processes;
}
