package org.alexmond.jsupervisor.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity class providing common fields for all entities.
 */
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * Unique identifier for the entity
     */
    private Long id;

    /**
     * Timestamp when the entity was created
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated
     */
    private LocalDateTime updatedAt;

    /**
     * Version number for optimistic locking
     */
    private Integer version;

    /**
     * Pre-persist callback to set created timestamp
     */
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 0;
    }

    /**
     * Pre-update callback to update timestamp
     */
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
