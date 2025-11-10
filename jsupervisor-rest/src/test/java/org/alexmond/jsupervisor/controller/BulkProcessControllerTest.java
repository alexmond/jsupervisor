package org.alexmond.jsupervisor.controller;

import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BulkProcessControllerTest {

    @InjectMocks
    private BulkProcessController bulkProcessController;

    @Mock
    private ProcessManagerBulk processManagerBulk;


    /**
     * Test to verify successful execution of the `startAll` method.
     * This case simulates when all processes are successfully started.
     */
    @Test
    void startAllProcessesSuccessfully() {
        // Arrange
        doNothing().when(processManagerBulk).startAll();

        // Act
        ResponseEntity<Void> response = bulkProcessController.startAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(processManagerBulk, times(1)).startAll();
    }

    /**
     * Test to verify the `startAll` method handles an IllegalStateException properly.
     * This case simulates when some processes are already running.
     */
    @Test
    void startAllProcessesConflict() {
        // Arrange
        doThrow(new IllegalStateException("Processes are already running")).when(processManagerBulk).startAll();

        // Act
        ResponseEntity<Void> response = bulkProcessController.startAll();

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(processManagerBulk, times(1)).startAll();
    }

    /**
     * Test to verify the `startAll` method handles generic exceptions properly.
     * This case simulates an unexpected error during the process.
     */
    @Test
    void startAllProcessesInternalError() {
        // Arrange
        doThrow(new RuntimeException("Unexpected error")).when(processManagerBulk).startAll();

        // Act
        ResponseEntity<Void> response = bulkProcessController.startAll();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(processManagerBulk, times(1)).startAll();
    }
}