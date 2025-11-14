package org.alexmond.jsupervisor.controller;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
@Slf4j
public class RestProcessManagerBulkTest {
    private String apiprefix = "/api/v1/processes/bulk";

    @Autowired
    private ProcessManagerBulk processManagerBulk;
    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void testStartRestartStop() throws Exception {

        String processName1 = "sleepTestProcess";
        String processName2 = "sleepTestProcess2";
        ProcessConfig processConfig1 = createBaseProcessConfig();
        processRepository.addProcess(processName1, processConfig1);
        ProcessConfig processConfig2 = createBaseProcessConfig();
        processRepository.addProcess(processName2, processConfig2);

        mockMvc.perform(post(apiprefix+"/start"))
                .andExpect(status().isOk())
                .andDo(document("start-all")
                );
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.running, process.getStatus());
        });
        mockMvc.perform(post(apiprefix+"/stop"))
                .andExpect(status().isOk())
                .andDo(document("stop-all")
                );
        Thread.sleep(100);
        processRepository.findAllProcessStatusRest().forEach(process -> {
            assertEquals(ProcessStatus.stopped, process.getStatus());
        });
        mockMvc.perform(post(apiprefix+"/restart"))
                .andExpect(status().isOk())
                .andDo(document("restart-all")
                );
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