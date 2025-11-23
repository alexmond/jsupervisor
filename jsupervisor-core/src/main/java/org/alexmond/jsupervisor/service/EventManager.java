package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Service component for managing process events.
 * Provides methods for querying, paginating, and filtering process event history.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventManager {

    private final EventRepository eventRepository;

    /**
     * Retrieves a page of events with customizable sorting and filtering.
     *
     * @param page          Page number (0-based)
     * @param size          Number of items per page
     * @param sortBy        Field to sort by (e.g., "eventTime", "processName", "newStatus")
     * @param sortDirection Sort direction ("asc" or "desc")
     * @return Page of ProcessEventEntry objects
     */
    public Page<ProcessEventEntry> getEvents(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            return eventRepository.findAll(pageable);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction '{}', defaulting to DESC", sortDirection);
            Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            return eventRepository.findAll(pageable);
        }
    }

    /**
     * Retrieves a page of events with default sorting by event time (newest first).
     *
     * @param page Page number (0-based)
     * @param size Number of items per page
     * @return Page of ProcessEventEntry objects sorted by event time descending
     */
    public Page<ProcessEventEntry> getEventsByPage(int page, int size) {
        return getEvents(page, size, "eventTime", "desc");
    }

    /**
     * Retrieves a page of events for a specific process.
     *
     * @param processName   Name of the process to filter by
     * @param page          Page number (0-based)
     * @param size          Number of items per page
     * @param sortBy        Field to sort by
     * @param sortDirection Sort direction ("asc" or "desc")
     * @return Page of ProcessEventEntry objects for the specified process
     */
    public Page<ProcessEventEntry> getEventsByProcessName(String processName, int page, int size,
                                                          String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return eventRepository.findByProcessName(processName, pageable);
    }

    /**
     * Retrieves events filtered by status with pagination.
     *
     * @param status        The process status to filter by
     * @param page          Page number (0-based)
     * @param size          Number of items per page
     * @param sortBy        Field to sort by
     * @param sortDirection Sort direction ("asc" or "desc")
     * @return Page of events matching the specified status
     */
    public Page<ProcessEventEntry> getEventsByStatus(ProcessStatus status, int page, int size,
                                                     String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return eventRepository.findByNewStatus(status, pageable);
    }

    /**
     * Retrieves an event by its unique identifier.
     *
     * @param id The ID of the event to retrieve
     * @return Optional containing the event if found, empty otherwise
     */
    public Optional<ProcessEventEntry> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Gets the total count of all events in the repository.
     *
     * @return Total number of events
     */
    public long getTotalEventsCount() {
        return eventRepository.count();
    }

    /**
     * Retrieves all events without pagination (use with caution for large datasets).
     *
     * @return Collection of all ProcessEventEntry objects
     */
    public Collection<ProcessEventEntry> getAllEvents() {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .toList();
    }

    /**
     * Deletes all events from the repository.
     * Use with caution - this operation cannot be undone.
     */
    public void clearAllEvents() {
        log.warn("Clearing all events from repository");
        eventRepository.deleteAll();
    }

    /**
     * Deletes a specific event by ID.
     *
     * @param id The ID of the event to delete
     * @return true if event was deleted, false if not found
     */
    public boolean deleteEventById(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            log.info("Deleted event with ID: {}", id);
            return true;
        }
        log.warn("Event with ID {} not found for deletion", id);
        return false;
    }
}
