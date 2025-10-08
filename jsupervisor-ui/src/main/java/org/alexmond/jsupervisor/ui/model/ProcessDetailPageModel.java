package org.alexmond.jsupervisor.ui.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusRest;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProcessDetailPageModel extends BaseModel {
  private ProcessStatusRest proc;
}
