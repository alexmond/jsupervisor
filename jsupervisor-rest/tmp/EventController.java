package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEvent;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for process event management.
 * Provides endpoints for querying process events and history.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Event Management", description = "APIs for accessing process events and history")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieve all process events")
    public ResponseEntity<List<ProcessEvent>> getAllEvents(
            @Parameter(description = "Limit number of events") @RequestParam(defaultValue = "100") int limit,
            @Parameter(description = "Filter by process name") @RequestParam(required = false) String processName,
            @Parameter(description = "Filter by status") @RequestParam(required = false) ProcessStatus status) {

        Iterable<ProcessEvent> events = eventRepository.findAll();

        // Apply filters
        if (processName != null) {
            events = ((List<ProcessEvent>) events).stream()
                    .filter(event -> processName.equals(event.getProcessName()))
                    .collect(Collectors.toList());
        }

        if (status != null) {
            events = ((List<ProcessEvent>) events).stream()
                    .filter(event -> status.equals(event.getNewStatus()))
                    .collect(Collectors.toList());
        }

        // Apply limit
        if (limit > 0 && ((List<ProcessEvent>) events).size() > limit) {
            events = ((List<ProcessEvent>) events).subList(Math.max(0, ((List<ProcessEvent>) events).size() - limit), ((List<ProcessEvent>) events).size());
        }

//        return ResponseEntity.ok(events);
        return null;
    }

    @GetMapping("/{processName}")
    @Operation(summary = "Get events for process", description = "Retrieve events for a specific process")
    public ResponseEntity<List<ProcessEvent>> getProcessEvents(
            @Parameter(description = "Process name") @PathVariable String processName,
            @Parameter(description = "Limit number of events") @RequestParam(defaultValue = "50") int limit) {
        List<ProcessEvent> events = ((List<ProcessEvent>) eventRepository.findAll()).stream()
                .filter(event -> processName.equals(event.getProcessName()))
                .collect(Collectors.toList());
        if (limit > 0 && events.size() > limit) {
            events = events.subList(Math.max(0, events.size() - limit), events.size());
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get events summary", description = "Get summary statistics of all events")
    public ResponseEntity<Map<String, Object>> getEventsSummary() {
        Iterable<ProcessEvent> events = eventRepository.findAll();
        Map<ProcessStatus, Long> statusCounts = ((List<ProcessEvent>) events).stream()
                .collect(Collectors.groupingBy(
                        ProcessEvent::getNewStatus,
                        Collectors.counting()
                ));

        Map<String, Long> processCounts = ((List<ProcessEvent>) events).stream()
                .collect(Collectors.groupingBy(
                        ProcessEvent::getProcessName,
                        Collectors.counting()
                ));

        List<ProcessEvent> eventsList = ((List<ProcessEvent>) events);
        return ResponseEntity.ok(Map.of(
                "totalEvents", eventsList.size(),
                "statusCounts", statusCounts,
                "processCounts", processCounts,
                "lastEvent", eventsList.isEmpty() ? null : eventsList.get(eventsList.size() - 1),
                "timestamp", System.currentTimeMillis()
        ));
    }
}
