package org.alexmond.supervisor.repository;

import org.alexmond.supervisor.config.SupervisorConfig;
import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.model.RunningProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Configuration
public class ProcessRepository {


    private final Map<String,RunningProcess> runningProcesses = new HashMap<>();

    @Autowired
    public ProcessRepository(SupervisorConfig supervisorConfig) {
        for(var processConfig : supervisorConfig.getProcess().entrySet()){
            runningProcesses.put(processConfig.getKey(),new RunningProcess(processConfig.getValue()));
        }
    }

    public Collection<ProcessStatusRest> findAllProcessStatusRest(){
        Collection<ProcessStatusRest> formatedProcesses = new ArrayList<>();
        for (var runningProcess : runningProcesses.entrySet()) {
            formatedProcesses.add(new ProcessStatusRest(runningProcess.getKey(),runningProcess.getValue()));
        }
        return formatedProcesses;
    }

    public Map<String,RunningProcess> findAll(){
        return runningProcesses;
    }

    public RunningProcess getRunningProcess(String name){ return runningProcesses.get(name); }

    public  ProcessStatusRest getRunningProcessRest(String name){ return new ProcessStatusRest(name, runningProcesses.get(name));}

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
