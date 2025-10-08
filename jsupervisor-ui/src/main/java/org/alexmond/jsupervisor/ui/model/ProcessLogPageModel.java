package org.alexmond.jsupervisor.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.alexmond.jsupervisor.model.ProcessStatusRest;

/**
 * Model class representing a page that displays process log information.
 * This class extends BaseModel and contains details about process logs,
 * including their type, location, and content.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProcessLogPageModel extends BaseModel {
    /**
     * Associated process status information
     */
    ProcessStatusRest pr;
    /**
     * Type of log stream (stdout or stderr)
     */
    private String logType;               // stdout|stderr
    /**
     * Name of the log file or "N/A" if not available
     */
    private String logFile;               // filename or "N/A"
    /**
     * Number of log lines requested to be displayed
     */
    private int lines;                    // requested lines
    /**
     * Actual content of the log file
     */
    private String logContent;            // file content
}
