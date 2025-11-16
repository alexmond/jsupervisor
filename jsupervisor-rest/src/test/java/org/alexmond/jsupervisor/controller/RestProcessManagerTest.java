package org.alexmond.jsupervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
@Slf4j
public class RestProcessManagerTest {

    private String apiprefix = "/api/v1/processes";
    private String docprefix = "api/v1/processes";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
    
    @Test
    void getAllProcessesTest() throws Exception {
        String processName1 = "sleepTestProcess1";
        String processName2 = "sleepTestProcess2";
        ProcessConfig processConfig = createBaseProcessConfig();
        processRepository.addProcess(processName1, processConfig);
        processRepository.addProcess(processName2, processConfig);

        MvcResult result = mockMvc.perform(get(apiprefix))
                .andExpect(status().isOk())
                .andDo(document(docprefix+"/GET",
                        responseFields(
                                fieldWithPath("[].name").description("Name of the process"),
                                fieldWithPath("[].status").description("Current status of the process (e.g., running, stopped, failed)"),
                                fieldWithPath("[].pid").optional().description("Process ID assigned by the OS; null if not running"),
                                fieldWithPath("[].startTime").optional().description("Timestamp when the process was started"),
                                fieldWithPath("[].endTime").optional().description("Timestamp when the process ended"),
                                fieldWithPath("[].exitCode").optional().description("Exit code returned by the process"),
                                fieldWithPath("[].processRuntime").optional().description("Process runtime duration (ISO-8601 duration, e.g., PT10S)"),
                                fieldWithPath("[].processUptime").optional().description("Human-readable uptime string"),
                                fieldWithPath("[].stdoutLogfile").optional().description("Path to the stdout log file"),
                                fieldWithPath("[].stderrLogfile").optional().description("Path to the stderr log file"),
                                fieldWithPath("[].failedErrorLog").optional().description("Error details when process failed to start"),
                                fieldWithPath("[].alive").description("Whether the process is considered alive (derived from pid)")
                        )))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProcessStatusRest[] status = objectMapper.readValue(jsonResponse, ProcessStatusRest[].class);
        
    }

    @Test
    void testStartStopRestartProcess() throws Exception {

        String processName = "sleepTestProcess";
        ProcessConfig processConfig = createBaseProcessConfig();
        processRepository.addProcess(processName, processConfig);

        mockMvc.perform(post(apiprefix+"/start/{name}", processName))
                .andExpect(status().isOk())
                .andDo(document(docprefix+"/start"+"/POST",
                        pathParameters(
                                parameterWithName("name").description("The process name to start")
                        )
                ));

        verifyProcessStatus(processName,1,ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        mockMvc.perform(post(apiprefix+"/restart/{name}", processName))
                .andExpect(status().isOk())
                .andDo(document("restart-process-by-name",
                        pathParameters(
                                parameterWithName("name").description("The process name to start")
                        )
                ));
        verifyProcessStatus(processName,1,ProcessStatus.running);
        assertEquals(ProcessStatus.running, processRepository.getRunningProcessRest(processName).getStatus());
        mockMvc.perform(post(apiprefix+"/stop/{name}", processName))
                .andExpect(status().isOk())
                .andDo(document("restart-process-by-name",
                        pathParameters(
                                parameterWithName("name").description("The process name to start")
                        )
                ));
        verifyProcessStatus(processName,1,ProcessStatus.stopped);
        assertEquals(ProcessStatus.stopped, processRepository.getRunningProcessRest(processName).getStatus());

        MvcResult result = mockMvc.perform(get(apiprefix + "/status/{name}", processName))
                .andExpect(status().isOk())
                .andDo(document("get-status-process-by-name",
                        pathParameters(
                                parameterWithName("name").description("The process name to get status for")
                        ),
                        responseFields(
                                fieldWithPath("name").description("Name of the process"),
                                fieldWithPath("status").description("Current status of the process (e.g., running, stopped, failed)"),
                                fieldWithPath("pid").optional().description("Process ID assigned by the OS; null if not running"),
                                fieldWithPath("startTime").optional().description("Timestamp when the process was started"),
                                fieldWithPath("endTime").optional().description("Timestamp when the process ended"),
                                fieldWithPath("exitCode").optional().description("Exit code returned by the process"),
                                fieldWithPath("processRuntime").optional().description("Process runtime duration (ISO-8601 duration, e.g., PT10S)"),
                                fieldWithPath("processUptime").optional().description("Human-readable uptime string"),
                                fieldWithPath("stdoutLogfile").optional().description("Path to the stdout log file"),
                                fieldWithPath("stderrLogfile").optional().description("Path to the stderr log file"),
                                fieldWithPath("failedErrorLog").optional().description("Error details when process failed to start"),
                                fieldWithPath("alive").description("Whether the process is considered alive (derived from pid)")
                        )))
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