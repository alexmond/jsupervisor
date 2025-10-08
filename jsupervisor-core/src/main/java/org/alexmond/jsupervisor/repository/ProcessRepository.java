package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class managing running processes in the supervisor system.
 * Provides methods to access and manipulate process information.
 */
@Configuration
public class ProcessRepository {


    private final Map<String, RunningProcess> runningProcesses = new ConcurrentHashMap<>();


    /**
     * Constructs a ProcessRepository and initializes running processes from configuration.
     *
     * @param supervisorConfig Configuration containing process definitions
     * @param eventRepository  Repository for handling process events
     */
    @Autowired
    public ProcessRepository(SupervisorConfig supervisorConfig, EventRepository eventRepository) {
        for (var processConfig : supervisorConfig.getProcess().entrySet()) {
            runningProcesses.put(processConfig.getKey(), new RunningProcess(processConfig.getKey(), processConfig.getValue(), eventRepository));
        }
    }

    /**
     * Retrieves status information for all running processes in REST format.
     *
     * @return Collection of ProcessStatusRest objects containing process status information
     */
    public Collection<ProcessStatusRest> findAllProcessStatusRest() {
        Collection<ProcessStatusRest> formatedProcesses = new ArrayList<>();
        for (var runningProcess : runningProcesses.entrySet()) {
            formatedProcesses.add(new ProcessStatusRest(runningProcess.getKey(), runningProcess.getValue()));
        }
        return formatedProcesses;
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
    public ProcessStatusRest getRunningProcessRest(String name) {
        return new ProcessStatusRest(name, runningProcesses.get(name));
    }

//    public RunningProcess addProcess(RunningProcess runningProcess){
//        runningProcesses.add(runningProcess);
//        return runningProcess;
//    }

//    public Optional<RunningProcess> updateProcess(int id, String newName){
//        Optional<RunningProcess> findUser =  runningProcesses.stream()
//                .filter(runningProcess -> runningProcess.getId()==id)
//                .findFirst();
//        findUser.ifPresent(runningProcess -> runningProcess.setName(newName));
//        return findUser;
//    }
//
//    public boolean deleteProcess(int id){
//        return runningProcesses.removeIf(runningProcess -> runningProcess.getId() == id);
//    }


}
