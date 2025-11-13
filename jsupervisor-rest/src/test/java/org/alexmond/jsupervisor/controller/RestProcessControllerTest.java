package org.alexmond.jsupervisor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the RestProcessController class.
 * This test ensures the correct functionality of the startAllProcess method.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

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