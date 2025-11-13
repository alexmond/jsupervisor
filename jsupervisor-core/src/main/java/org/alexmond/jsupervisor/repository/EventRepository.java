package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing ProcessEvent entities in the database.
 * Provides basic CRUD operations and additional query methods for process events storage.
 * Extends both CrudRepository and PagingAndSortingRepository to support basic operations
 * and pagination capabilities.
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "/api/v1/events", path = "events")
public interface EventRepository extends CrudRepository<ProcessEventEntry, Long>,
        PagingAndSortingRepository<ProcessEventEntry, Long> {

    /**
     * Saves a ProcessEventEntry entity to the repository.
     * This method is not exposed via REST API.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    @RestResource(exported = false)
    <S extends ProcessEventEntry> S save(S entity);

    /**
     * Deletes a ProcessEventEntry from the repository.
     * This method is not exposed via REST API.
     *
     * @param entity the entity to delete
     */
    @RestResource(exported = false)
    void delete(ProcessEventEntry entity);

    /**
     * Finds all process events associated with a specific process name.
     *
     * @param processName the name of the process to search for
     * @return a list of ProcessEventEntry objects matching the process name
     */
    @RestResource(path = "byProcessName", rel = "byProcessName")
    List<ProcessEventEntry> findByProcessName(String processName);

    /**
     * Finds all process events with a specific status.
     *
     * @param status the ProcessStatus to search for
     * @return a list of ProcessEventEntry objects matching the status
     */
    @RestResource(path = "byStatus", rel = "byStatus")
    List<ProcessEventEntry> findByNewStatus(ProcessStatus status);
}
