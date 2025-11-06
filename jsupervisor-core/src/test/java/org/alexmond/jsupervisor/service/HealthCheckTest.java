package org.alexmond.jsupervisor.service;

import org.alexmond.jsupervisor.config.*;
import org.alexmond.jsupervisor.healthcheck.PortHealthCheck;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.waitAtMost;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the ProcessManagerBulk class.
 * This class verifies the behavior of the restartAll method.
 */
@SpringBootTest
public class HealthCheckTest {
    @Autowired
    private ProcessManagerBulk processManagerBulk;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private ProcessRepository processRepository;

    @Test
    void testActuatorHealthCheck() throws InterruptedException {
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("sleep");
        processConfig.setArgs(List.of("100000"));
        processConfig.setWorkingDirectory(".");
        processConfig.setHealthCheckType(HealthCheckType.ACTUATOR);
        ActuatorHealthCheckConfig actuatorHealthCheckConfig = new ActuatorHealthCheckConfig();
        actuatorHealthCheckConfig.setActuatorHealthUrl("http://localhost:8086/actuator/health");
        processConfig.setActuatorHealthCheck(actuatorHealthCheckConfig);

        processRepository.addProcess("acTest",processConfig);
        processManager.startProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> processRepository.getRunningProcessRest("acTest").getStatus().equals(ProcessStatus.healthy));
        assertEquals(ProcessStatus.healthy, processRepository.getRunningProcessRest("acTest").getStatus());
        processManager.stopProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> !processRepository.getRunningProcessRest("acTest").isAlive());
        processRepository.removeProcess("acTest");
    }

    @Test
    void testHttpHealthCheck() throws InterruptedException {
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("sleep");
        processConfig.setArgs(List.of("100000"));
        processConfig.setWorkingDirectory(".");
        processConfig.setHealthCheckType(HealthCheckType.HTTP);
        HttpHealthCheckConfig httpHealthCheckConfig = new HttpHealthCheckConfig();
        httpHealthCheckConfig.setUrl("http://localhost:8086/actuator/health");
        processConfig.setHttpHealthCheckConfig(httpHealthCheckConfig);

        processRepository.addProcess("acTest",processConfig);
        processManager.startProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> processRepository.getRunningProcessRest("acTest").getStatus().equals(ProcessStatus.healthy));
        assertEquals(ProcessStatus.healthy, processRepository.getRunningProcessRest("acTest").getStatus());
        processManager.stopProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> !processRepository.getRunningProcessRest("acTest").isAlive());
        processRepository.removeProcess("acTest");
    }

    @Test
    void testPortHealthCheck() throws InterruptedException {
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setCommand("sleep");
        processConfig.setArgs(List.of("100000"));
        processConfig.setWorkingDirectory(".");
        processConfig.setHealthCheckType(HealthCheckType.PORT);
        PortHealthCheckConfig portHealthCheckConfig = new PortHealthCheckConfig();
        portHealthCheckConfig.setHost("localhost");
        portHealthCheckConfig.setPort(8086);
        processConfig.setPortHealthCheck(portHealthCheckConfig);

        processRepository.addProcess("acTest",processConfig);
        processManager.startProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> processRepository.getRunningProcessRest("acTest").getStatus().equals(ProcessStatus.healthy));
        assertEquals(ProcessStatus.healthy, processRepository.getRunningProcessRest("acTest").getStatus());
        processManager.stopProcess("acTest");
        await().atMost(1, TimeUnit.MINUTES)
                .until(() -> !processRepository.getRunningProcessRest("acTest").isAlive());
        processRepository.removeProcess("acTest");
    }
}