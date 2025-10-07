package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.model.ProcessEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class EventRepository {
    private final Map<Long, ProcessEvent> data = new TreeMap<>();
    private Long nextId = 1L;


    public ProcessEvent save(ProcessEvent entity) {
        if (entity.getId() == null) {
            entity.setId(nextId++);
        }
        data.put(entity.getId(), entity);
        return entity;
    }

    public ProcessEvent findById(Long id) {
        return data.get(id);
    }

    public List<ProcessEvent> findAll() {
        return new ArrayList<>(data.values());
    }

    // Implement other CrudRepository methods (findAll, delete, etc.)
}
