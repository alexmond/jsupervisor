package org.alexmond.sample;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomHealthIndicatorTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomHealthIndicator customHealthIndicator;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        customHealthIndicator.setHealthy(true);
    }

    @Test
    void healthShouldReturnUpWhenHealthyIsTrue() throws Exception {
        // Arrange
        mockMvc.perform(get("/setUp")).andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.custom.details.custom").value("Everything is OK!"));
    }

    @Test
    void healthShouldReturnDownWhenHealthyIsFalse() throws Exception {
        // Arrange
        mockMvc.perform(get("/setDown")).andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.components.custom.details.custom").value("Something is wrong!"));
    }
}