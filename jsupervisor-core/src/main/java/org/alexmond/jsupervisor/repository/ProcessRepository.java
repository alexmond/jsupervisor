package org.alexmond.jsupervisor.repository;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ProcessRepository {


    private final Map<String, RunningProcess> runningProcesses = new ConcurrentHashMap<>();


    @Autowired
    public ProcessRepository(SupervisorConfig supervisorConfig, EventRepository eventRepository) {
        for (var processConfig : supervisorConfig.getProcess().entrySet()) {
            runningProcesses.put(processConfig.getKey(), new RunningProcess(processConfig.getKey(), processConfig.getValue(), eventRepository));
        }
    }

    public Collection<ProcessStatusRest> findAllProcessStatusRest() {
        Collection<ProcessStatusRest> formatedProcesses = new ArrayList<>();
        for (var runningProcess : runningProcesses.entrySet()) {
            formatedProcesses.add(new ProcessStatusRest(runningProcess.getKey(), runningProcess.getValue()));
        }
        return formatedProcesses;
    }

    public Map<String, RunningProcess> findAll() {
        return runningProcesses;
    }

    public RunningProcess getRunningProcess(String name) {
        return runningProcesses.get(name);
    }

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
