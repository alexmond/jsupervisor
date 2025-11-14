package org.alexmond.jsupervisor.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.apache.commons.io.FileUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Map;


/**
 * Represents a REST resource for supervisor system information.
 * This class provides system hardware and operating system details
 * such as memory usage, CPU information, and node configuration.
 */
@Schema(description = "Supervisor system information including hardware and OS details")
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorRest {

    /**
     * The name of the node in the supervisor system
     */
    @Schema(description = "The name of the node in the supervisor system", example = "supervisor-1")
    private String nodeName;
    /**
     * Description of the node
     */
    @Schema(description = "Description of the node", example = "Main supervisor node")
    private String description;
    /**
     * Total physical memory available in the system
     */
    @Schema(description = "Total physical memory available in the system", example = "16 GB")
    private String physicalMemory;
    /**
     * Number of physical CPU cores
     */
    @Schema(description = "Number of physical CPU cores", example = "8")
    private Integer physicalCpu;
    /**
     * Operating system information
     */
    @Schema(description = "Operating system information", example = "Windows 10 Pro")
    private String operatingSystem;
    /**
     * Currently available memory in the system
     */
    @Schema(description = "Currently available memory in the system", example = "8.5 GB")
    private String availableMemory;

    /**
     * Constructs a new SupervisorRest instance.
     * Initializes system information including CPU, memory, and OS details.
     *
     * @param supervisorConfig the configuration for the supervisor system
     */
    public SupervisorRest(SupervisorConfig supervisorConfig) {
        nodeName = supervisorConfig.getNodeName();
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

    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.convertValue(this, Map.class);
    }
}
