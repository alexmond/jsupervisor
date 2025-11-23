package org.alexmond.jsupervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.SupervisorRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class RestSupervisorControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SupervisorConfig supervisorConfig;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSupervisorInfo_WithValidData_ReturnsSupervisorInfo() throws Exception {
        // Arrange
        supervisorConfig.setNodeName("TestNode");
        supervisorConfig.setDescription("Test Description");
        supervisorConfig.setProcess(Map.of("Process1", new ProcessConfig(), "Process2", new ProcessConfig()));

        MvcResult result = mockMvc.perform(get("/api/v1/supervisor/info"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        SupervisorRest info = objectMapper.readValue(jsonResponse, SupervisorRest.class);

        assertEquals("TestNode", info.getNodeName());
        assertEquals("Test Description", info.getDescription());
        assertTrue(info.getPhysicalCpu() != null);
    }
}