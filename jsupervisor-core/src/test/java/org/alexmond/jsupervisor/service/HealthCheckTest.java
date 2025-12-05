package org.alexmond.jsupervisor.service;

import org.alexmond.jsupervisor.config.*;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HealthCheckTest {
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;

    private ProcessConfig createBaseProcessConfig() {
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("sleep");
        processConfig.setArgs(List.of("100000"));
        processConfig.setWorkingDirectory(".");
        return processConfig;
    }

    private void verifyProcessHealth(String processName, ProcessStatus expectedStatus) {
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> processRepository.getRunningProcessInfo(processName).getStatus().equals(expectedStatus));
        assertEquals(expectedStatus, processRepository.getRunningProcessInfo(processName).getStatus());
    }

    private void cleanupProcess(String processName) {
        processManager.stopProcess(processName);
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> !processRepository.getRunningProcessInfo(processName).isAlive());
        processRepository.removeProcess(processName);
    }

    private void setupHealthCheckDefaults(AbstractHealthCheckConfig config) {
        config.setSuccessThreshold(1);
        config.setFailureThreshold(1);
        config.setPeriodSeconds(1);
    }

    private void executeHealthCheckTest(ProcessConfig processConfig, ProcessStatus expectedStatus) {
        processRepository.addProcess("acTest", processConfig);
        processManager.startProcess("acTest");
        verifyProcessHealth("acTest", expectedStatus);
        cleanupProcess("acTest");
    }

    @Test
    void testActuatorHealthCheckUp() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.ACTUATOR);
        ActuatorHealthCheckConfig actuatorHealthCheckConfig = new ActuatorHealthCheckConfig();
        actuatorHealthCheckConfig.setActuatorHealthUrl("http://localhost:9086/healthDown");
        setupHealthCheckDefaults(actuatorHealthCheckConfig);
        processConfig.setActuatorHealthCheck(actuatorHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.unhealthy);
    }

    @Test
    void testActuatorHealthCheckDown() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.ACTUATOR);
        ActuatorHealthCheckConfig actuatorHealthCheckConfig = new ActuatorHealthCheckConfig();
        actuatorHealthCheckConfig.setActuatorHealthUrl("http://localhost:9086/actuator/health");
        setupHealthCheckDefaults(actuatorHealthCheckConfig);
        processConfig.setActuatorHealthCheck(actuatorHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.healthy);
    }

    @Test
    void testHttpHealthCheck() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.HTTP);
        HttpHealthCheckConfig httpHealthCheckConfig = new HttpHealthCheckConfig();
        httpHealthCheckConfig.setUrl("http://localhost:9086/actuator/health");
        setupHealthCheckDefaults(httpHealthCheckConfig);
        processConfig.setHttpHealthCheckConfig(httpHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.healthy);
    }

    @Test
    void testHttpHealthCheckNon200returnCode() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.HTTP);
        HttpHealthCheckConfig httpHealthCheckConfig = new HttpHealthCheckConfig();
        httpHealthCheckConfig.setUrl("http://localhost:9086/healthDown");
        setupHealthCheckDefaults(httpHealthCheckConfig);
        processConfig.setHttpHealthCheckConfig(httpHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.unhealthy);
    }


    @Test
    void testPortHealthCheck() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.PORT);
        PortHealthCheckConfig portHealthCheckConfig = new PortHealthCheckConfig();
        portHealthCheckConfig.setHost("localhost");
        portHealthCheckConfig.setPort(9086);
        setupHealthCheckDefaults(portHealthCheckConfig);
        processConfig.setPortHealthCheck(portHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.healthy);
    }


    @Test
    void testActuatorHealthCheckFail() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.ACTUATOR);
        ActuatorHealthCheckConfig actuatorHealthCheckConfig = new ActuatorHealthCheckConfig();
        actuatorHealthCheckConfig.setActuatorHealthUrl("http://localhost:1000/actuator/health");
        setupHealthCheckDefaults(actuatorHealthCheckConfig);
        processConfig.setActuatorHealthCheck(actuatorHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.unhealthy);
    }


    @Test
    void testHttpHealthCheckFail() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.HTTP);
        HttpHealthCheckConfig httpHealthCheckConfig = new HttpHealthCheckConfig();
        httpHealthCheckConfig.setUrl("http://localhost:1000/actuator/health");
        setupHealthCheckDefaults(httpHealthCheckConfig);
        processConfig.setHttpHealthCheckConfig(httpHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.unhealthy);
    }

    @Test
    void testPortHealthCheckFail() {
        ProcessConfig processConfig = createBaseProcessConfig();
        processConfig.setHealthCheckType(HealthCheckType.PORT);
        PortHealthCheckConfig portHealthCheckConfig = new PortHealthCheckConfig();
        portHealthCheckConfig.setHost("localhost");
        portHealthCheckConfig.setPort(1000);
        setupHealthCheckDefaults(portHealthCheckConfig);
        processConfig.setPortHealthCheck(portHealthCheckConfig);
        executeHealthCheckTest(processConfig, ProcessStatus.unhealthy);
    }
}