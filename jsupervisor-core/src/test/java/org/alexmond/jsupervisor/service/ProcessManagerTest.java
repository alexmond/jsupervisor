package org.alexmond.jsupervisor.service;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
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
        String processName = "sleepTestProcess";
        ProcessConfig processConfig = createBaseProcessConfig();
        processRepository.addProcess(processName, processConfig);

        processManager.startProcess(processName);
        verifyProcessStatus(processName, 1, ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessInfo(processName).getStatus());
        processManager.restartProcess(processName);
        verifyProcessStatus(processName, 1, ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessInfo(processName).getStatus());
        processManager.stopProcess(processName);
        verifyProcessStatus(processName, 1, ProcessStatus.stopped);
        assertEquals(ProcessStatus.stopped, processRepository.getRunningProcessInfo(processName).getStatus());
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
                .until(() -> processRepository.getRunningProcessInfo(processName).getStatus().equals(expectedStatus));
        assertEquals(expectedStatus, processRepository.getRunningProcessInfo(processName).getStatus());
    }

}