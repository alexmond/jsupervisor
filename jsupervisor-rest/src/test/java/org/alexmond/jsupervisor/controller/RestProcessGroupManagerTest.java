package org.alexmond.jsupervisor.controller;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessGroupManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class RestProcessGroupManagerTest {
    private String apiprefix = "/api/v1/processes/group";

    @Autowired
    private ProcessGroupManager processGroupManager;
    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testStartRestartStop() throws Exception {

        String processName1 = "sleepTestProcess";
        String processName2 = "sleepTestProcess2";
        ProcessConfig processConfig1 = createBaseProcessConfig();
        processRepository.addProcess(processName1, processConfig1);
        ProcessConfig processConfig2 = createBaseProcessConfig();
        processRepository.addProcess(processName2, processConfig2);

        mockMvc.perform(post(apiprefix + "/start"))
                .andExpect(status().isOk());
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
        mockMvc.perform(post(apiprefix + "/stop"))
                .andExpect(status().isOk());
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.stopped, process.getStatus());
        });
        mockMvc.perform(post(apiprefix + "/restart"))
                .andExpect(status().isOk());
        Thread.sleep(1000);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
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