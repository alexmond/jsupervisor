package org.alexmond.jsupervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.SupervisorRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
@Slf4j
class RestSupervisorControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private SupervisorConfig supervisorConfig;

    @Autowired
    private MockMvc mockMvc;

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
    void testGetSupervisorInfo_WithValidData_ReturnsSupervisorInfo() throws Exception {
        // Arrange
        supervisorConfig.setNodeName("TestNode");
        supervisorConfig.setDescription("Test Description");
        supervisorConfig.setProcess(Map.of("Process1", new ProcessConfig(), "Process2", new ProcessConfig()));

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/supervisor/info"))
                .andExpect(status().isOk())
                .andDo(document("get-supervisor-info",
                        responseFields(
                                fieldWithPath("nodeName").description("The name of the node in the supervisor system"),
                                fieldWithPath("description").optional().description("Description of the node"),
                                fieldWithPath("physicalMemory").description("Total physical memory available in the system (human-readable)"),
                                fieldWithPath("physicalCpu").description("Number of physical CPU cores"),
                                fieldWithPath("operatingSystem").description("Operating system information"),
                                fieldWithPath("availableMemory").optional().description("Currently available memory in the system (human-readable)")
                        )))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        SupervisorRest info = objectMapper.readValue(jsonResponse, SupervisorRest.class);

        assertEquals("TestNode", info.getNodeName());
        assertEquals("Test Description", info.getDescription());
        assertTrue(info.getPhysicalCpu() != null);
    }
}