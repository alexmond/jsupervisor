package org.alexmond.jsupervisor.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;
import org.alexmond.jsupervisor.model.RunningProcess;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class managing running processes in the supervisor system.
 * Provides methods to access and manipulate process information.
 */
@Slf4j
public class ProcessRepository {


    private final Map<String, RunningProcess> runningProcesses = new ConcurrentHashMap<>();
    @Getter
    private final Map<Integer, List<String>> processOrders = new TreeMap<>();
    @Getter
    private final Map<String, List<String>> processGroups = new HashMap<>();
    private final ApplicationEventPublisher eventPublisher;


    /**
     * Constructs a ProcessRepository and initializes running processes from configuration.
     *
     * @param supervisorConfig Configuration containing process definitions
     * @param eventPublisher   Application event publisher for process events
     */
    public ProcessRepository(SupervisorConfig supervisorConfig, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        for (var processConfig : supervisorConfig.getProcess().entrySet()) {
            addProcess(processConfig.getKey(), processConfig.getValue());
        }
    }

    /**
     * Adds a new process to the repository with specified configuration.
     * Process is added to running processes map and ordered list based on its priority.
     *
     * @param processName   Name of the process to add
     * @param processConfig Configuration for the process
     */
    public void addProcess(String processName, ProcessConfig processConfig) {
        runningProcesses.put(processName, new RunningProcess(processName, processConfig, eventPublisher));
        // Handle order
        int order = Integer.MAX_VALUE;
        if (processConfig.getOrder() != null) {
            order = processConfig.getOrder();
        }
        if (processOrders.containsKey(order)) {
            processOrders.get(order).add(processName);
        } else {
            processOrders.put(order, new ArrayList<>(List.of(processName)));
        }
        
        //Handle groups
        if(processConfig.getGroups() != null) {
            for (String group : processConfig.getGroups()) {
                if (processGroups.containsKey(group)) {
                    processGroups.get(group).add(processName);
                } else {
                    processGroups.put(group, new ArrayList<>(List.of(processName)));
                }
            }
        }
    }

    /**
     * Removes a process from the repository if it exists and is not running.
     * Process is removed from both running processes map and ordered list.
     *
     * @param processName Name of the process to remove
     */
    public void removeProcess(String processName) {
        if (runningProcesses.containsKey(processName) && !runningProcesses.get(processName).isProcessRunning()) {
            runningProcesses.remove(processName);
            Iterator<Map.Entry<Integer, List<String>>> iterator = processOrders.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<String>> entry = iterator.next();
                entry.getValue().remove(processName);
                if (entry.getValue().isEmpty()) {
                    iterator.remove();
                }
            }
        } else {
            log.error("Process {} is running or does not exist", processName);
        }
    }

    /**
     * Retrieves status information for all running processes in REST format.
     *
     * @return Collection of ProcessStatusRest objects containing process status information
     */
    public Collection<ProcessStatusInfo> findAllProcessInfo() {
        Collection<ProcessStatusInfo> formatedProcesses = new ArrayList<>();
        for (var runningProcess : runningProcesses.entrySet()) {
            formatedProcesses.add(getRunningProcessInfo(runningProcess.getKey()));
        }
        return formatedProcesses;
    }

    public Collection<ProcessStatusInfo> findGroupProcessInfo(String group) {
        Collection<ProcessStatusInfo> formatedProcesses = new ArrayList<>();
        processGroups.get(group).forEach(
                processName -> formatedProcesses.add(getRunningProcessInfo(processName))
        );
        return formatedProcesses;
    }

    public Map<Integer, List<String>>  getProcessGroupOrders(String groupName) {
        List<String> processesInGroup = processGroups.get(groupName);
        Map<Integer, List<String>> groupProcessOrders = new TreeMap<>();
        processOrders.forEach((order, processes) -> {
            var groupProcesses = processes.stream().filter(processesInGroup::contains).toList();
            if (!groupProcesses.isEmpty())
                groupProcessOrders.put(order, groupProcesses);
        });
        return groupProcessOrders;
    }

    public Map<Integer, List<String>>  getAutostartProcessOrders() {
        Map<Integer, List<String>> autostartProcessOrders = new TreeMap<>();
        processOrders.forEach((order, processes) -> {
            var groupProcesses = processes.stream()
                    .filter(name -> runningProcesses.get(name).isAutoStart())
                    .toList();
            if (!groupProcesses.isEmpty())
                autostartProcessOrders.put(order, groupProcesses);
        });
        return autostartProcessOrders;
    }


    /**
     * Retrieves all running processes.
     *
     * @return Map of process names to RunningProcess objects
     */
    public Map<String, RunningProcess> findAll() {
        return runningProcesses;
    }

    /**
     * Retrieves a specific running process by name.
     *
     * @param name Name of the process to retrieve
     * @return RunningProcess object for the specified name
     */
    public RunningProcess getRunningProcess(String name) {
        return runningProcesses.get(name);
    }

    /**
     * Retrieves status information for a specific process in REST format.
     *
     * @param name Name of the process to retrieve status for
     * @return ProcessStatusRest object containing process status information
     */
    public ProcessStatusInfo getRunningProcessInfo(String name) {
        return new ProcessStatusInfo(name, runningProcesses.get(name));
    }


}
