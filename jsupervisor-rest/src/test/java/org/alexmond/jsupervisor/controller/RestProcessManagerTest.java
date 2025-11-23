package org.alexmond.jsupervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class RestProcessManagerTest {

    @Autowired
    ObjectMapper objectMapper;
    private String apiprefix = "/api/v1/processes";
    private String docprefix = "api/v1/processes";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;

    @Test
    void getAllProcessesTest() throws Exception {
        String processName1 = "sleepTestProcess1";
        String processName2 = "sleepTestProcess2";
        ProcessConfig processConfig = createBaseProcessConfig();
        processRepository.addProcess(processName1, processConfig);
        processRepository.addProcess(processName2, processConfig);

        MvcResult result = mockMvc.perform(get(apiprefix))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProcessStatusRest[] status = objectMapper.readValue(jsonResponse, ProcessStatusRest[].class);

    }

    @Test
    void testStartStopRestartProcess() throws Exception {

        String processName = "sleepTestProcess";
        ProcessConfig processConfig = createBaseProcessConfig();
        processRepository.addProcess(processName, processConfig);

        mockMvc.perform(post(apiprefix + "/start/{name}", processName))
                .andExpect(status().isOk());

        verifyProcessStatus(processName, 1, ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        mockMvc.perform(post(apiprefix + "/restart/{name}", processName))
                .andExpect(status().isOk());
        verifyProcessStatus(processName, 1, ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        mockMvc.perform(post(apiprefix + "/stop/{name}", processName))
                .andExpect(status().isOk());
        verifyProcessStatus(processName, 1, ProcessStatus.stopped);
        assertEquals(ProcessStatus.stopped, processRepository.getRunningProcessRest(processName).getStatus());

        MvcResult result = mockMvc.perform(get(apiprefix + "/status/{name}", processName))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProcessStatusRest status = objectMapper.readValue(jsonResponse, ProcessStatusRest.class);

        assertEquals(ProcessStatus.stopped, status.getStatus());

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