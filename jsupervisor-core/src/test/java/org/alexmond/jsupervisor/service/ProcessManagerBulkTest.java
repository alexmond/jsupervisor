package org.alexmond.jsupervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class ProcessManagerBulkTest {
    @Autowired
    private ProcessManagerBulk processManagerBulk;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private SupervisorConfig supervisorConfig;
    @Autowired
    private EventRepository eventRepository;

    @Test
    void testRestartAllInvokesStopAllAndStartAll() throws InterruptedException {

        processManagerBulk.startAll();
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
        processManagerBulk.stopAll();
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.stopped, process.getStatus());
        });
        processManagerBulk.restartAll();
        Thread.sleep(1000);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
    }

    @Test
    void testStartupOrder() throws InterruptedException {
        supervisorConfig.setAutoStartDelay(Duration.ofSeconds(10));
        var processConfig1 = createBaseProcessConfig();
        processConfig1.setOrder(1);
        var processConfig2 = createBaseProcessConfig();
        processConfig2.setOrder(2);
        var processConfig3 = createBaseProcessConfig();
        processConfig3.setOrder(3);
        processRepository.addProcess("acTest1", processConfig1);
        processRepository.addProcess("acTest2", processConfig2);
        processRepository.addProcess("acTest3", processConfig3);
        processManagerBulk.startAll();
        verifyProcessStatus("acTest3", 5, ProcessStatus.running);
        var acTest1Status = processRepository.getRunningProcessRest("acTest1").getStartTime();
        var acTest2Status = processRepository.getRunningProcessRest("acTest2").getStartTime();
        var acTest3Status = processRepository.getRunningProcessRest("acTest3").getStartTime();
        log.info("Process start times - acTest1: {}, acTest2: {}, acTest3: {}", acTest1Status, acTest2Status, acTest3Status);
        assertTrue(Duration.between(acTest1Status, acTest2Status).toSeconds() >= 8);
        assertTrue(Duration.between(acTest2Status, acTest3Status).toSeconds() >= 8);
        processManagerBulk.stopAll();
        verifyProcessStatus("acTest3", 5, ProcessStatus.stopped);
        processRepository.removeProcess("acTest1");
        processRepository.removeProcess("acTest2");
        processRepository.removeProcess("acTest3");
    }

    @Test
    void testAutostartAndOrder() throws InterruptedException {
        supervisorConfig.setAutoStartDelay(Duration.ofSeconds(10));
        supervisorConfig.setAutoStart(true);
        var processConfig1 = createBaseProcessConfig();
        processConfig1.setOrder(1);
        processConfig1.setAutoStart(true);
        var processConfig2 = createBaseProcessConfig();
        processConfig2.setOrder(2);
        var processConfig3 = createBaseProcessConfig();
        processConfig3.setOrder(3);
        processConfig3.setAutoStart(true);
        processRepository.addProcess("acTest1", processConfig1);
        processRepository.addProcess("acTest2", processConfig2);
        processRepository.addProcess("acTest3", processConfig3);
        processManagerBulk.autoStartAll();
        verifyProcessStatus("acTest3", 5, ProcessStatus.running);
        assertEquals(ProcessStatus.not_started, processRepository.getRunningProcessRest("acTest2").getStatus());
        var acTest1Status = processRepository.getRunningProcessRest("acTest1").getStartTime();
        var acTest3Status = processRepository.getRunningProcessRest("acTest3").getStartTime();
        log.info("Process start times - acTest1: {}, acTest3: {}", acTest1Status, acTest3Status);
        assertTrue(Duration.between(acTest1Status, acTest3Status).toSeconds() >= 8);
        processManagerBulk.stopAll();
        verifyProcessStatus("acTest3", 5, ProcessStatus.stopped);
        processRepository.removeProcess("acTest1");
        processRepository.removeProcess("acTest2");
        processRepository.removeProcess("acTest3");
    }

    @Test
    void testRestartAllHandlesErrors() {
        // Arrange
        ProcessRepository processRepository = mock(ProcessRepository.class);
        ProcessManager processManager = mock(ProcessManager.class);
        SupervisorConfig config = mock(SupervisorConfig.class);

        ProcessManagerBulk processManagerBulk = Mockito.spy(new ProcessManagerBulk(processRepository, processManager, config));

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

    private ProcessConfig createBaseProcessConfig() {
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("sleep");
        processConfig.setArgs(List.of("100000"));
        processConfig.setWorkingDirectory(".");
        return processConfig;
    }

    private void verifyProcessStatus(String processName, Integer minutes, ProcessStatus expectedStatus) {
        await().atMost(minutes, TimeUnit.MINUTES)
                .until(() -> processRepository.getRunningProcessRest(processName).getStatus().equals(expectedStatus));
        assertEquals(expectedStatus, processRepository.getRunningProcessRest(processName).getStatus());
    }
}