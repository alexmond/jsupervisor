package org.alexmond.jsupervisor.controller;

import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the LogController class focusing on the `getStdoutLog` method.
 * This test class covers various scenarios to ensure the correctness of the method's implementation.
 */
@SpringBootTest
@AutoConfigureMockMvc
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessRepository processRepository;

    @Test
    void testGetStdoutLog_ProcessNotFound() throws Exception {
        // Mock behavior
        when(processRepository.getRunningProcess(anyString())).thenReturn(null);

        // Perform the test
        mockMvc.perform(get("/api/v1/logs/nonexistentProcess/stdout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStdoutLog_LogFileDoesNotExist() throws Exception {
        // Mock behavior
        RunningProcess runningProcess = Mockito.mock(RunningProcess.class);
        when(runningProcess.getStdout()).thenReturn(null);
        when(processRepository.getRunningProcess("testProcess")).thenReturn(runningProcess);

        // Perform the test
        mockMvc.perform(get("/api/v1/logs/testProcess/stdout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processName", is("testProcess")))
                .andExpect(jsonPath("$.logType", is("stdout")))
                .andExpect(jsonPath("$.content", is("")))
                .andExpect(jsonPath("$.lines", is(0)))
                .andExpect(jsonPath("$.file", is("N/A")));
    }

//    @Test
//    void testGetStdoutLog_ReadLogFileSuccessfully(@TempDir Path tempDir) throws Exception {
//        // Create a real temporary file with test content
//        Path logFile = tempDir.resolve("stdout.log");
//        Files.write(logFile, List.of("Line 1", "Line 2", "Line 3"));
//
//        File mockFile = logFile.toFile();
//        long lastModified = mockFile.lastModified();
//
//        // Mock behavior
//        RunningProcess runningProcess = Mockito.mock(RunningProcess.class);
//        when(runningProcess.getStdout()).thenReturn(mockFile);
//        when(processRepository.getRunningProcess("testProcess")).thenReturn(runningProcess);
//
//        // Perform the test
//        mockMvc.perform(get("/api/v1/logs/testProcess/stdout")
//                        .param("lines", "2")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.processName", is("testProcess")))
//                .andExpect(jsonPath("$.logType", is("stdout")))
//                .andExpect(jsonPath("$.content", is("Line 2\nLine 3")))
//                .andExpect(jsonPath("$.lines", is(2)))
//                .andExpect(jsonPath("$.totalLines", is(3)))
//                .andExpect(jsonPath("$.file", is("stdout.log")))
//                .andExpect(jsonPath("$.lastModified", is(lastModified)));
//    }

}