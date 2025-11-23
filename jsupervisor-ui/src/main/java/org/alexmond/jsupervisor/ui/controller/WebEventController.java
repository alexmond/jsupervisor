package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.service.EventManager;
import org.alexmond.jsupervisor.ui.model.EventsPageModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Controller handling web interface operations for process event management.
 * Provides endpoints for viewing and paginating through process event history.
 */
@Slf4j
@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class WebEventController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private final EventManager eventManager;
    private final EventRepository eventRepository;
    /**
     * Flag indicating whether to return direct page templates
     */
    private final boolean linkToPage = false;

    /**
     * Displays the event history page with pagination support and process name filtering.
     *
     * @param page              Page number (0-based), defaults to 0
     * @param size              Number of items per page, defaults to 20
     * @param sortBy            Field to sort by, defaults to "eventTime"
     * @param sortDirection     Sort direction (asc/desc), defaults to "desc"
     * @param filterProcessName Process name to filter by (optional)
     * @param model             Spring MVC model
     * @return View name to render
     */
    @GetMapping
    public String getEventsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "eventTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String filterProcessName,
            Model model) {

        log.debug("Fetching events page: page={}, size={}, sortBy={}, sortDirection={}, filterProcessName={}",
                page, size, sortBy, sortDirection, filterProcessName);

        // Ensure page is not negative
        if (page < 0) {
            page = 0;
        }

        // Limit page size
        if (size < 1) {
            size = DEFAULT_PAGE_SIZE;
        } else if (size > 100) {
            size = 100;
        }

        // Fetch events with or without filter
        Page<ProcessEventEntry> eventsPage;
        if (filterProcessName != null && !filterProcessName.isEmpty() && !filterProcessName.equals("all")) {
            eventsPage = eventManager.getEventsByProcessName(filterProcessName, page, size, sortBy, sortDirection);
        } else {
            eventsPage = eventManager.getEvents(page, size, sortBy, sortDirection);
            filterProcessName = null; // Reset to null for "All" option
        }

        // Calculate page numbers for pagination (show 5 pages: current +/- 2)
        List<Integer> pageNumbers = calculatePageNumbers(page, eventsPage.getTotalPages());

        // Get all distinct process names for the dropdown
        List<String> availableProcessNames = getDistinctProcessNames();

        EventsPageModel pageModel = EventsPageModel.builder()
                .title("Process Events")
                .activePage("events")
                .eventsPage(eventsPage)
                .currentPage(page)
                .totalPages(eventsPage.getTotalPages())
                .totalElements(eventsPage.getTotalElements())
                .pageSize(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .pageNumbers(pageNumbers)
                .filterProcessName(filterProcessName)
                .availableProcessNames(availableProcessNames)
                .build();

        model.addAttribute("pageModel", pageModel);
        model.addAttribute("eventsPage", eventsPage);
        model.addAttribute("content", "proc/events");

        if (linkToPage) return "proc/events";
        return "layout";
    }

    /**
     * Calculates the list of page numbers to display in pagination controls.
     * Shows up to 5 page numbers centered around the current page.
     *
     * @param currentPage Current page number (0-based)
     * @param totalPages  Total number of pages
     * @return List of page numbers to display
     */
    private List<Integer> calculatePageNumbers(int currentPage, int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();

        // Calculate start and end page numbers (show current +/- 2)
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        // Generate list of page numbers
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        return pageNumbers;
    }

    /**
     * Retrieves a list of distinct process names from all events.
     * Used to populate the filter dropdown.
     *
     * @return List of unique process names sorted alphabetically
     */
    private List<String> getDistinctProcessNames() {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .map(ProcessEventEntry::getProcessName)
                .distinct()
                .sorted()
                .toList();
    }
}