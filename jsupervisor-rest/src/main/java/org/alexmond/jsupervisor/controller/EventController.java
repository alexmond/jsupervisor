package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.service.EventManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for process event management.
 * Provides endpoints for querying, filtering, and managing process events with pagination support.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Event Management", description = "Operations for managing and querying process event history")
public class EventController {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final EventManager eventManager;

    /**
     * Get paginated list of all events.
     */
    @GetMapping
    @Operation(
            summary = "Get paginated events",
            description = "Retrieves a paginated list of all process events with customizable sorting",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved events",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
            }
    )
    public ResponseEntity<Page<ProcessEventEntry>> getEvents(
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(type = "integer", minimum = "0"))
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "20", schema = @Schema(type = "integer", minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Field to sort by", example = "eventTime", schema = @Schema(allowableValues = {"eventTime", "processName", "newStatus", "oldStatus", "pid"}))
            @RequestParam(defaultValue = "eventTime") String sortBy,

            @Parameter(description = "Sort direction", example = "desc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "desc") String sortDirection) {

        if (size > MAX_PAGE_SIZE) {
            log.warn("Requested page size {} exceeds maximum {}, using maximum", size, MAX_PAGE_SIZE);
            size = MAX_PAGE_SIZE;
        }

        Page<ProcessEventEntry> events = eventManager.getEvents(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(events);
    }

    /**
     * Get a specific event by ID.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get event by ID",
            description = "Retrieves detailed information about a specific process event",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event found"),
                    @ApiResponse(responseCode = "404", description = "Event not found")
            }
    )
    public ResponseEntity<ProcessEventEntry> getEventById(
            @Parameter(description = "Event ID", required = true, example = "123")
            @PathVariable Long id) {

        Optional<ProcessEventEntry> event = eventManager.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get events by process name with pagination.
     */
    @GetMapping("/by-process/{processName}")
    @Operation(
            summary = "Get events by process name",
            description = "Retrieves paginated events for a specific process",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved events"),
                    @ApiResponse(responseCode = "404", description = "Process not found")
            }
    )
    public ResponseEntity<Page<ProcessEventEntry>> getEventsByProcess(
            @Parameter(description = "Process name", required = true, example = "myapp")
            @PathVariable String processName,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Field to sort by", example = "eventTime")
            @RequestParam(defaultValue = "eventTime") String sortBy,

            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection) {

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        Page<ProcessEventEntry> events = eventManager.getEventsByProcessName(
                processName, page, size, sortBy, sortDirection
        );
        return ResponseEntity.ok(events);
    }

    /**
     * Get events by status with pagination.
     */
    @GetMapping("/by-status/{status}")
    @Operation(
            summary = "Get events by status",
            description = "Retrieves paginated events matching a specific process status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved events"),
                    @ApiResponse(responseCode = "400", description = "Invalid status value")
            }
    )
    public ResponseEntity<Page<ProcessEventEntry>> getEventsByStatus(
            @Parameter(
                    description = "Process status to filter by",
                    required = true,
                    example = "running",
                    schema = @Schema(
                            type = "string",
                            allowableValues = {"not_started", "running", "finished", "unknown", "failed",
                                    "failed_to_start", "stopped", "stopping", "aborted",
                                    "starting", "healthy", "unhealthy"}
                    )
            )
            @PathVariable ProcessStatus status,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Field to sort by", example = "eventTime")
            @RequestParam(defaultValue = "eventTime") String sortBy,

            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection) {

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        Page<ProcessEventEntry> events = eventManager.getEventsByStatus(
                status, page, size, sortBy, sortDirection
        );
        return ResponseEntity.ok(events);
    }

    /**
     * Get total event count.
     */
    @GetMapping("/count")
    @Operation(
            summary = "Get total event count",
            description = "Returns the total number of events in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved count")
            }
    )
    public ResponseEntity<Map<String, Long>> getTotalEventsCount() {
        long count = eventManager.getTotalEventsCount();
        return ResponseEntity.ok(Map.of("totalEvents", count));
    }

}
