package org.alexmond.jsupervisor.model;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for SupervisorRest.
 * This class verifies the functionality of the toMap method, which converts
 * a SupervisorRest instance into a map representation.
 */
@SpringBootTest
class SupervisorRestTest {
    
    @Autowired
    private SupervisorConfig supervisorConfig;

    @Test
    void testToMap_ValidData() {
        // Arrange: Setup necessary data for SupervisorRest
        supervisorConfig = new SupervisorConfig();
        supervisorConfig.setNodeName("supervisor-1");
        supervisorConfig.setDescription("Main Supervisor Node");

        SupervisorRest supervisorRest = new SupervisorRest(supervisorConfig);
        supervisorRest.setPhysicalMemory("16 GB");
        supervisorRest.setPhysicalCpu(8);
        supervisorRest.setOperatingSystem("Windows 10 Pro");
        supervisorRest.setAvailableMemory("8 GB");

        // Act: Call the toMap method
        Map<String, Object> resultMap = supervisorRest.toMap();

        // Assert: Verify the map content
        assertEquals("supervisor-1", resultMap.get("nodeName"));
        assertEquals("Main Supervisor Node", resultMap.get("description"));
        assertEquals("16 GB", resultMap.get("physicalMemory"));
        assertEquals(8, resultMap.get("physicalCpu"));
        assertEquals("Windows 10 Pro", resultMap.get("operatingSystem"));
        assertEquals("8 GB", resultMap.get("availableMemory"));
    }

    @Test
    void testToMap_MissingOptionalFields() {
        // Arrange: Setup SupervisorRest with minimal data
        supervisorConfig = new SupervisorConfig();
        supervisorConfig.setNodeName("supervisor-2");
        supervisorConfig.setDescription("Backup Supervisor Node");

        SupervisorRest supervisorRest = new SupervisorRest(supervisorConfig);
        supervisorRest.setPhysicalMemory(null);
        supervisorRest.setPhysicalCpu(null);
        supervisorRest.setOperatingSystem("Linux");
        supervisorRest.setAvailableMemory(null);

        // Act: Call the toMap method
        Map<String, Object> resultMap = supervisorRest.toMap();

        // Assert: Verify the map content
        assertEquals("supervisor-2", resultMap.get("nodeName"));
        assertEquals("Backup Supervisor Node", resultMap.get("description"));
        assertNull(resultMap.get("physicalMemory"));
        assertNull(resultMap.get("physicalCpu"));
        assertEquals("Linux", resultMap.get("operatingSystem"));
        assertNull(resultMap.get("availableMemory"));
    }

    @Test
    void testToMap_WithCustomValues() {
        // Arrange: Setup SupervisorRest with custom data
        SupervisorRest supervisorRest = new SupervisorRest(mock(SupervisorConfig.class));
        supervisorRest.setNodeName("custom-node");
        supervisorRest.setDescription("Custom Supervisor Node");
        supervisorRest.setPhysicalMemory("32 GB");
        supervisorRest.setPhysicalCpu(12);
        supervisorRest.setOperatingSystem("Ubuntu 20.04");
        supervisorRest.setAvailableMemory("20 GB");

        // Act: Call the toMap method
        Map<String, Object> resultMap = supervisorRest.toMap();

        // Assert: Verify the map content
        assertEquals("custom-node", resultMap.get("nodeName"));
        assertEquals("Custom Supervisor Node", resultMap.get("description"));
        assertEquals("32 GB", resultMap.get("physicalMemory"));
        assertEquals(12, resultMap.get("physicalCpu"));
        assertEquals("Ubuntu 20.04", resultMap.get("operatingSystem"));
        assertEquals("20 GB", resultMap.get("availableMemory"));
    }
}