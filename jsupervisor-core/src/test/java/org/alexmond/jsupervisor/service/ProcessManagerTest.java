package org.alexmond.jsupervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessEvent;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class ProcessManagerTest {

    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    EventRepository eventRepository;

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