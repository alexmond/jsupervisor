package org.alexmond.jsupervisor.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;


@Component
@Data
@Slf4j
public class SupervisorRest {

    private String NodeName;
    private String description;
    private String physicalMemory;
    private Integer physicalCpu;
    private String operatingSystem;
    private String availableMemory;

    @Autowired
    public SupervisorRest(SupervisorConfig supervisorConfig) {
        NodeName = supervisorConfig.getNodeName();
        description = supervisorConfig.getDescription();

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        OperatingSystem os = systemInfo.getOperatingSystem();
        operatingSystem = os.toString();


        // CPU Information
        CentralProcessor processor = hardware.getProcessor();
        physicalCpu = hardware.getProcessor().getPhysicalProcessorCount();

        log.info("CPU Identifier: {}", processor.getProcessorIdentifier().getName());
        log.info("Physical Cores: {}", processor.getPhysicalProcessorCount());
        log.info("Logical Processors: {}", processor.getLogicalProcessorCount());


        // Memory Information
        GlobalMemory memory = hardware.getMemory();
        physicalMemory = FileUtils.byteCountToDisplaySize(memory.getTotal());
        log.info("Total Physical Memory: {} bytes", memory.getTotal());
        log.info("Available Physical Memory: {} bytes", memory.getAvailable());

        log.info("System Identifier: {}", systemInfo);
    }

    public void refresh() {
        SystemInfo systemInfo = new SystemInfo();
        availableMemory = FileUtils.byteCountToDisplaySize(systemInfo.getHardware().getMemory().getAvailable());
    }

}
