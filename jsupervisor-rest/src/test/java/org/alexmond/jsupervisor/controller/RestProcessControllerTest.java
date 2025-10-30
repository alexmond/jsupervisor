package org.alexmond.jsupervisor.controller;

import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.times;

/**
 * Unit tests for the RestProcessController class.
 * This test ensures the correct functionality of the startAllProcess method.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestProcessControllerTest {

//    @Autowired
//    private RestProcessController restProcessController;

    @Autowired
    private MockMvc mockMvc;

//    /**
//     * Test case to verify that the startAllProcess method calls
//     * ProcessManagerBulk's startAll method exactly once.
//     */
//    @Test
//    void testStartAllProcess() throws IOException {
//        // Act
//        restProcessController.startAllProcess();
//
//        // Assert
//        Mockito.verify(processManagerBulk, times(1)).startAll();
//    }

    @Test
    void generateOpenApiSpec() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andDo(print())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    Files.write(Paths.get("openapi.json"), content.getBytes());
                })
                .andExpect(status().isOk());
    }

}