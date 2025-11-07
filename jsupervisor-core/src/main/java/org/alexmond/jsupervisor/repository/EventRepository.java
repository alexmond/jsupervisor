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
 * Repository for managing ProcessEvent entities.
 * Provides basic CRUD operations for process events storage.
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "/api/v1/events", path = "events")
public interface EventRepository extends CrudRepository<ProcessEventEntry, Long>,
        PagingAndSortingRepository<ProcessEventEntry, Long> {

    @RestResource(exported = false)
    <S extends ProcessEventEntry> S save(S entity);

    @RestResource(exported = false)
    void delete(ProcessEventEntry entity);

    @RestResource(path = "byProcessName", rel = "byProcessName")
    List<ProcessEventEntry> findByProcessName(String processName);

    // Example: Find by status (add more as needed)
    @RestResource(path = "byStatus", rel = "byStatus")
    List<ProcessEventEntry> findByNewStatus(ProcessStatus status);
}
