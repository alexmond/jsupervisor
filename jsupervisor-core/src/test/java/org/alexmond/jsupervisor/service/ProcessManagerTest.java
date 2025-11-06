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
    void testStartProcessWithValidConfiguration() throws IOException {
        // Mock a valid process configuration
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("echo");
        processConfig.setArgs(List.of("Hello, World!"));
        processConfig.setWorkingDirectory(".");

        processRepository.addProcess("testProcess",processConfig);
        // Call startProcess method
        processManager.startProcess("testProcess");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<ProcessEventEntry> events = eventRepository.findByProcessName("testProcess");
        log.info("events: {}", events);
        assertEquals(3, events.stream().count());
    }

//    @Test
//    void testStartProcessThrowsExceptionWhenNoConfig() {
//        SupervisorConfig supervisorConfig = mock(SupervisorConfig.class);
//        ProcessRepository processRepository = mock(ProcessRepository.class);
//        ProcessManagerMonitor processManagerMonitor = mock(ProcessManagerMonitor.class);
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = mock(ThreadPoolTaskScheduler.class);
//        ProcessManager processManager = new ProcessManager(supervisorConfig, processRepository, processManagerMonitor, threadPoolTaskScheduler);
//
//        // Create RunningProcess with required constructor parameters but null config
//        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
//        ProcessConfig processConfig = new ProcessConfig(); // Create a config first
//        RunningProcess runningProcess = spy(new RunningProcess("invalidProcess", processConfig, eventPublisher));
//
//        // Then override to return null
//        doReturn(null).when(runningProcess).getProcessConfig();
//        when(processRepository.getRunningProcess("invalidProcess")).thenReturn(runningProcess);
//
//        // Verify exception is thrown
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> processManager.startProcess("invalidProcess"));
//        assertEquals("No process config found for: invalidProcess", exception.getMessage());
//    }
//
//    @Test
//    void testStartProcessProcessAlreadyRunning() {
//        SupervisorConfig supervisorConfig = mock(SupervisorConfig.class);
//        ProcessRepository processRepository = mock(ProcessRepository.class);
//        ProcessManagerMonitor processManagerMonitor = mock(ProcessManagerMonitor.class);
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = mock(ThreadPoolTaskScheduler.class);
//        ProcessManager processManager = new ProcessManager(supervisorConfig, processRepository, processManagerMonitor, threadPoolTaskScheduler);
//
//        // Mock process already running
//        ProcessConfig processConfig = new ProcessConfig();
//        processConfig.setCommand("echo");
//        processConfig.setArgs(List.of("Hello, World!"));
//        processConfig.setWorkingDirectory(".");
//
//        // Create RunningProcess with required constructor parameters
//        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
//        RunningProcess runningProcess = spy(new RunningProcess("testProcess", processConfig, eventPublisher));
//
//        Process mockProcess = mock(Process.class);
//        when(mockProcess.pid()).thenReturn(12345L);
//        when(mockProcess.isAlive()).thenReturn(true);
//
//        // Override the process to simulate it's already running
//        doReturn(mockProcess).when(runningProcess).getProcess();
//        when(processRepository.getRunningProcess("testProcess")).thenReturn(runningProcess);
//
//        // Call startProcess method
//        processManager.startProcess("testProcess");
//
//        // Verify log entry for already running process
//        verify(runningProcess, never()).reset();
//        verify(runningProcess, never()).setProcessStatus(ProcessStatus.starting);
//        verifyNoInteractions(processManagerMonitor);
//    }

//    @Test
//    void testStartProcessIOException() throws IOException {
//        SupervisorConfig supervisorConfig = mock(SupervisorConfig.class);
//        ProcessRepository processRepository = mock(ProcessRepository.class);
//        ProcessManagerMonitor processManagerMonitor = mock(ProcessManagerMonitor.class);
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = mock(ThreadPoolTaskScheduler.class);
//        ProcessManager processManager = new ProcessManager(supervisorConfig, processRepository, processManagerMonitor, threadPoolTaskScheduler);
//
//        // Mock valid configuration with IOException during process start
//        ProcessConfig processConfig = new ProcessConfig();
//        processConfig.setCommand("invalid-command");
//        processConfig.setArgs(List.of());
//        processConfig.setWorkingDirectory(".");
//
//        // Use a REAL RunningProcess object instead of a mock
//        RunningProcess runningProcess = new RunningProcess();
//        runningProcess.setProcessConfig(processConfig);
//
//        File mockStdOut = Files.createTempFile("test_stdout", ".log").toFile();
//        File mockStdErr = Files.createTempFile("test_stderr", ".log").toFile();
//        runningProcess.setStdout(mockStdOut);
//        runningProcess.setStderr(mockStdErr);
//
//        when(processRepository.getRunningProcess("testProcess")).thenReturn(runningProcess);
//
//        // Call startProcess method - this will naturally throw IOException
//        processManager.startProcess("testProcess");
//
//        // Give it a moment to complete since it's @Async
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        // Verify process status updates
//        assertEquals(ProcessStatus.failed, runningProcess.getProcessStatus());
//        assertNotNull(runningProcess.getFailedErrorLog());
//        assertNull(runningProcess.getProcess());
//
//        // Cleanup
//        mockStdOut.delete();
//        mockStdErr.delete();
//    }
}