package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.model.ProcessEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Repository for managing ProcessEvent entities.
 * Provides basic CRUD operations for process events storage.
 */
public class EventRepository {
    /**
     * Storage for ProcessEvent entities with their IDs as keys
     */
    private final Map<Long, ProcessEvent> data = new TreeMap<>();
    /**
     * Counter for generating next entity ID
     */
    private Long nextId = 1L;


    /**
     * Saves a ProcessEvent entity.
     * If the entity has no ID, generates a new one.
     *
     * @param entity the process event to save
     * @return the saved process event
     */
    public ProcessEvent save(ProcessEvent entity) {
        if (entity.getId() == null) {
            entity.setId(nextId++);
        }
        data.put(entity.getId(), entity);
        return entity;
    }

    /**
     * Retrieves a ProcessEvent by its ID.
     *
     * @param id the ID of the process event
     * @return the ProcessEvent if found, null otherwise
     */
    public ProcessEvent findById(Long id) {
        return data.get(id);
    }

    /**
     * Retrieves all ProcessEvent entities.
     *
     * @return list of all process events
     */
    public List<ProcessEvent> findAll() {
        return new ArrayList<>(data.values());
    }

    // Implement other CrudRepository methods (findAll, delete, etc.)
}
