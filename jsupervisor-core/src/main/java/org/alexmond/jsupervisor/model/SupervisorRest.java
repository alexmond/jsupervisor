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


/**
 * Represents a REST resource for supervisor system information.
 * This class provides system hardware and operating system details
 * such as memory usage, CPU information, and node configuration.
 */
@Component
@Data
@Slf4j
public class SupervisorRest {

    /**
     * The name of the node in the supervisor system
     */
    private String NodeName;
    /**
     * Description of the node
     */
    private String description;
    /**
     * Total physical memory available in the system
     */
    private String physicalMemory;
    /**
     * Number of physical CPU cores
     */
    private Integer physicalCpu;
    /**
     * Operating system information
     */
    private String operatingSystem;
    /**
     * Currently available memory in the system
     */
    private String availableMemory;

    /**
     * Constructs a new SupervisorRest instance.
     * Initializes system information including CPU, memory, and OS details.
     *
     * @param supervisorConfig the configuration for the supervisor system
     */
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

    /**
     * Refreshes the available memory information.
     * Updates the current available memory value from the system.
     */
    public void refresh() {
        SystemInfo systemInfo = new SystemInfo();
        availableMemory = FileUtils.byteCountToDisplaySize(systemInfo.getHardware().getMemory().getAvailable());
    }

}
