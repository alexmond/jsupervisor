package org.alexmond.supervisor.controller;

import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.model.RunningProcess;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.alexmond.supervisor.service.ProcessManager;
import org.alexmond.supervisor.service.ProcessManagerBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

@Controller
@RequestMapping("/proc")
public class WebProcessController {

    private final ProcessRepository processRepository;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;

    public WebProcessController(ProcessRepository processRepository, ProcessManager processManager, ProcessManagerBulk processManagerBulk) {
        this.processRepository = processRepository;
        this.processManager = processManager;
        this.processManagerBulk = processManagerBulk;
    }

    @GetMapping
    public String listUProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        model.addAttribute("processes", processes);
        model.addAttribute("title", "Process List");
        return "proc/list";
    }

    @GetMapping("/start/{name}")
    public String startProcess(@PathVariable String name) {
        processManager.startProcess(name);
        return "redirect:/proc";
    }

    @GetMapping("/stop/{name}")
    public String stopProcess(@PathVariable String name) {
        processManager.stopProcess(name);
        return "redirect:/proc";
    }

    @GetMapping("/restart/{name}")
    public String restartProcess(@PathVariable String name) {
        processManager.restartProcess(name);
        return "redirect:/proc";
    }

    @GetMapping("/startAll")
    public String startAll() throws IOException {
        processManagerBulk.startAll();
        return "redirect:/proc";
    }

    @GetMapping("/stopAll")
    public String stopAll() throws IOException {
        processManagerBulk.stopAll();
        return "redirect:/proc";
    }

    @GetMapping("/details/{name}")
    public String processesDetails(@PathVariable String name,Model model) {
        ProcessStatusRest proc = new ProcessStatusRest(name,processRepository.getRunningProcess(name));
        model.addAttribute("proc", proc);
        model.addAttribute("title", "Process Details");
        return "proc/detail";
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
        return "proc/process-log";
    }

    @GetMapping("/refresh")
    public String refresh() throws IOException {
        return "redirect:/proc";
    }

}