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
public class ProcessLogPageModel extends BaseModel {
  private String logType;               // stdout|stderr
  private String logFile;               // filename or "N/A"
  private int lines;                    // requested lines
  private String logContent;            // file content
  ProcessStatusRest pr;
}
