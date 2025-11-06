package org.alexmond.jsupervisor.service;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest
public class ProcessManagerBulkTest {
    @Autowired
    private ProcessManagerBulk processManagerBulk;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;

    @Test
    void testRestartAllInvokesStopAllAndStartAll() throws InterruptedException {

        processManagerBulk.startAll();
        Thread.sleep(1000);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
        processManagerBulk.stopAll();
        Thread.sleep(1000);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.stopped, process.getStatus());
        });
        processManagerBulk.restartAll();
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
    }

    @Test
    void testRestartAllHandlesErrors() {
        // Arrange
        ProcessRepository processRepository = mock(ProcessRepository.class);
        ProcessManager processManager = mock(ProcessManager.class);
        SupervisorConfig config = mock(SupervisorConfig.class);

        ProcessManagerBulk processManagerBulk = Mockito.spy(new ProcessManagerBulk(processRepository, processManager));
        processManagerBulk.config = config;

        doNothing().when(processManagerBulk).stopAll();
        // Use RuntimeException instead of InterruptedException since startAll() doesn't declare checked exceptions
        doThrow(new RuntimeException("Unexpected error")).when(processManagerBulk).startAll();

        // Act & Assert - verify that even with an exception, the method attempts to call both operations
        try {
            processManagerBulk.restartAll();
        } catch (RuntimeException e) {
            // Exception is expected
        }

        verify(processManagerBulk, times(1)).stopAll();
        verify(processManagerBulk, times(1)).startAll();
    }
}