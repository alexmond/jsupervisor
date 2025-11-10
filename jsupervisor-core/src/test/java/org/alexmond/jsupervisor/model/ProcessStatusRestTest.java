package org.alexmond.jsupervisor.model;

import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for ProcessStatusRest.
 * This class verifies the functionality of the toMap method, which converts
 * a ProcessStatusRest instance into a map representation.
 */
class ProcessStatusRestTest {

    @Test
    void testToMap_ValidData() {
        // Arrange: Setup necessary data
        ProcessConfig processConfig = mock(ProcessConfig.class);
        RunningProcess runningProcess = mock(RunningProcess.class);

        when(runningProcess.getStartTime()).thenReturn(LocalDateTime.of(2025, 11, 8, 14, 30));
        when(runningProcess.getEndTime()).thenReturn(LocalDateTime.of(2025, 11, 8, 16, 30));
        when(runningProcess.getExitCode()).thenReturn(0);
        when(runningProcess.getProcessConfig()).thenReturn(processConfig);
        when(runningProcess.getProcessRuntime()).thenReturn(Duration.ofHours(2));
        when(runningProcess.getProcessRuntimeFormatted()).thenReturn("2h");
        when(runningProcess.getStdoutLogfile()).thenReturn("/logs/stdout.log");
        when(runningProcess.getStderrLogfile()).thenReturn("/logs/stderr.log");
        when(runningProcess.getFailedErrorLog()).thenReturn(null);
        when(runningProcess.getProcessStatus()).thenReturn(ProcessStatus.running);

        ProcessStatusRest processStatusRest = new ProcessStatusRest("testProcess", runningProcess);

        // Act: Call the toMap method
        Map<String, Object> resultMap = processStatusRest.toMap();

        // Assert: Verify the map content
        assertEquals("testProcess", resultMap.get("name"));
        assertEquals("running", resultMap.get("status").toString());
        assertEquals(0, resultMap.get("exitCode"));
//        assertEquals(LocalDateTime.of(2025, 11, 8, 14, 30), resultMap.get("startTime"));
//        assertEquals(LocalDateTime.of(2025, 11, 8, 16, 30), resultMap.get("endTime"));
//        assertEquals(Duration.ofHours(2), resultMap.get("processRuntime"));
        assertEquals("2h", resultMap.get("processUptime"));
        assertEquals("/logs/stdout.log", resultMap.get("stdoutLogfile"));
        assertEquals("/logs/stderr.log", resultMap.get("stderrLogfile"));
        assertNull(resultMap.get("failedErrorLog"));
    }

    @Test
    void testToMap_MissingOptionalFields() {
        // Arrange: Setup minimal required data
        RunningProcess runningProcess = mock(RunningProcess.class);

        when(runningProcess.getStartTime()).thenReturn(LocalDateTime.of(2025, 11, 8, 12, 0));
        when(runningProcess.getEndTime()).thenReturn(null);
        when(runningProcess.getExitCode()).thenReturn(null);
        when(runningProcess.getProcessConfig()).thenReturn(null);
        when(runningProcess.getProcessRuntime()).thenReturn(null);
        when(runningProcess.getProcessRuntimeFormatted()).thenReturn(null);
        when(runningProcess.getStdoutLogfile()).thenReturn(null);
        when(runningProcess.getStderrLogfile()).thenReturn(null);
        when(runningProcess.getFailedErrorLog()).thenReturn("Error during startup");
        when(runningProcess.getProcessStatus()).thenReturn(ProcessStatus.failed);

        ProcessStatusRest processStatusRest = new ProcessStatusRest("processWithErrors", runningProcess);

        // Act: Call the toMap method
        Map<String, Object> resultMap = processStatusRest.toMap();

        // Assert: Verify the map content
        assertEquals("processWithErrors", resultMap.get("name"));
        assertEquals("failed", resultMap.get("status").toString());
        assertNull(resultMap.get("exitCode"));
//        assertEquals(LocalDateTime.of(2025, 11, 8, 12, 0), resultMap.get("startTime"));
        assertNull(resultMap.get("endTime"));
        assertNull(resultMap.get("processRuntime"));
        assertNull(resultMap.get("processUptime"));
        assertNull(resultMap.get("stdoutLogfile"));
        assertNull(resultMap.get("stderrLogfile"));
        assertEquals("Error during startup", resultMap.get("failedErrorLog"));
    }

    @Test
    void testToMap_ProcessWithPid() {
        // Arrange: Setup a process with a PID
        RunningProcess runningProcess = mock(RunningProcess.class);

        when(runningProcess.getProcess()).thenReturn(mock(Process.class));
        when(runningProcess.getProcess().pid()).thenReturn(12345L);
        when(runningProcess.getStartTime()).thenReturn(LocalDateTime.now());
        when(runningProcess.getProcessStatus()).thenReturn(ProcessStatus.running);

        ProcessStatusRest processStatusRest = new ProcessStatusRest("processWithPid", runningProcess);

        // Act: Call the toMap method
        Map<String, Object> resultMap = processStatusRest.toMap();

        // Assert: Ensure PID is included
        assertEquals(12345L, resultMap.get("pid"));
        assertEquals("processWithPid", resultMap.get("name"));
        assertEquals("running", resultMap.get("status").toString());
    }

    @Test
    void testToMap_NoPid() {
        // Arrange: Setup a process without a PID
        RunningProcess runningProcess = mock(RunningProcess.class);

        when(runningProcess.getProcess()).thenReturn(null);
        when(runningProcess.getStartTime()).thenReturn(LocalDateTime.now());
        when(runningProcess.getProcessStatus()).thenReturn(ProcessStatus.stopped);

        ProcessStatusRest processStatusRest = new ProcessStatusRest("processWithoutPid", runningProcess);

        // Act: Call the toMap method
        Map<String, Object> resultMap = processStatusRest.toMap();

        // Assert: Ensure PID is null
        assertNull(resultMap.get("pid"));
        assertEquals("processWithoutPid", resultMap.get("name"));
        assertEquals("stopped", resultMap.get("status").toString());
    }
}