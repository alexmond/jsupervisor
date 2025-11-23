package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing ProcessEvent entities in the database.
 * Provides basic CRUD operations and additional query methods for process events storage.
 * Extends both CrudRepository and PagingAndSortingRepository to support basic operations
 * and pagination capabilities.
 */
@Repository
public interface EventRepository extends CrudRepository<ProcessEventEntry, Long>,
        PagingAndSortingRepository<ProcessEventEntry, Long> {

    /**
     * Finds all process events associated with a specific process name.
     *
     * @param processName the name of the process to search for
     * @param pageable    pagination and sorting information
     * @return a page of ProcessEventEntry objects matching the process name
     */
    Page<ProcessEventEntry> findByProcessName(String processName, Pageable pageable);

    /**
     * Finds all process events with a specific status.
     *
     * @param status   the ProcessStatus to search for
     * @param pageable pagination and sorting information
     * @return a page of ProcessEventEntry objects matching the status
     */
    Page<ProcessEventEntry> findByNewStatus(ProcessStatus status, Pageable pageable);

    /**
     * Finds all events with pagination support.
     *
     * @param pageable pagination and sorting information
     * @return a page of all ProcessEventEntry objects
     */
    @Override
    Page<ProcessEventEntry> findAll(Pageable pageable);
}
