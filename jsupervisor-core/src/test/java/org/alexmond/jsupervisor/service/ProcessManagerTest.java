package org.alexmond.jsupervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class ProcessManagerTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void testStartStopRestartProcess() throws InterruptedException {
        String processName = "sleep1";
        processManager.startProcess(processName);
        Thread.sleep(100);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        processManager.restartProcess(processName);
        Thread.sleep(100);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        processManager.stopProcess(processName);
        Thread.sleep(100);
        assertEquals(ProcessStatus.stopped, processRepository.getRunningProcessRest(processName).getStatus());
    }

}