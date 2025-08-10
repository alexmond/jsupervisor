package org.alexmond.supervisor.controller;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.SupervisorConfig;
import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.repository.RunningProcess;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.alexmond.supervisor.service.ProcessManager;
import org.alexmond.supervisor.service.ProcessManagerBulk;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/")
public class WebProcessController {

    private final ProcessRepository processRepository;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;
    private final SupervisorConfig supervisorConfig;

    public WebProcessController(ProcessRepository processRepository, ProcessManager processManager,
                                ProcessManagerBulk processManagerBulk, SupervisorConfig supervisorConfig) {
        this.processRepository = processRepository;
        this.processManager = processManager;
        this.processManagerBulk = processManagerBulk;
        this.supervisorConfig = supervisorConfig;
    }

    @GetMapping({"/","/index"})
    public String getAllProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        model.addAttribute("processes", processes);
        model.addAttribute("title", "Process List");
        model.addAttribute("content", "proc/list");
        model.addAttribute("uiconfig", supervisorConfig.getUiConfig());
        return "layout";
    }

    @GetMapping("/zzz")
    public String zzzgetAllProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        model.addAttribute("processes", processes);
        model.addAttribute("title", "Process List");
        model.addAttribute("content", "proc/list");
        model.addAttribute("uiconfig", supervisorConfig.getUiConfig());
        return "proc/list";
    }


    @GetMapping("/start/{name}")
    public String startProcess(@PathVariable String name) {
        processManager.startProcess(name);
        return "redirect:/";
    }

    @GetMapping("/stop/{name}")
    public String stopProcess(@PathVariable String name) {
        processManager.stopProcess(name);
        return "redirect:/";
    }

    @GetMapping("/restart/{name}")
    public String restartProcess(@PathVariable String name) {
        processManager.restartProcess(name);
        return "redirect:/";
    }

    @GetMapping("/startAll")
    public String startAll() throws IOException {
        processManagerBulk.startAll();
        return "redirect:/";
    }

    @GetMapping("/stopAll")
    public String stopAll() throws IOException {
        processManagerBulk.stopAll();
        return "redirect:/";
    }

    @GetMapping("/details/{name}")
    public String processesDetails(@PathVariable String name,Model model) {
        ProcessStatusRest proc = new ProcessStatusRest(name,processRepository.getRunningProcess(name));
        model.addAttribute("proc", proc);
        model.addAttribute("title", "Process Details");
        model.addAttribute("content", "proc/detail");
        return "layout";
    }

    @GetMapping("/log/{name}")
    public String processLog(@PathVariable String name, Model model) throws IOException {
        RunningProcess runningProcess = processRepository.getRunningProcess(name);
        String stdoutContents = "";
        if (runningProcess != null && runningProcess.getStdout() != null && runningProcess.getStdout().exists()) {
            stdoutContents = Files.readString(runningProcess.getStdout().toPath());
        }
        ProcessStatusRest proc = new ProcessStatusRest(name,processRepository.getRunningProcess(name));
        model.addAttribute("pr", proc);
        model.addAttribute("stdoutContents", stdoutContents);
        model.addAttribute("title", "Process Log");
        model.addAttribute("content", "proc/process-log");
        model.addAttribute("uiconfig", supervisorConfig.getUiConfig());
        return "layout";
    }

}