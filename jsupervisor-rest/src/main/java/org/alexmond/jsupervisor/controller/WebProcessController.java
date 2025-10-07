package org.alexmond.jsupervisor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.config.UiConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebProcessController {

    private final ProcessRepository processRepository;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;
    private final SupervisorConfig supervisorConfig;
    private final EventRepository eventRepository;
    private final UiConfig uiConfig;

    private boolean link = false;


    @GetMapping({"/", "/index"})
    public String getAllProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        model.addAttribute("processes", processes);
        model.addAttribute("title", "Process List");
        model.addAttribute("content", "proc/list");
        if (link) {return "proc/list";}
        return "layout";
    }

    @GetMapping("/zzz")
    public String zzzgetAllProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        model.addAttribute("processes", processes);
        model.addAttribute("title", "Process List");
        model.addAttribute("content", "proc/list");
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
    public String processesDetails(@PathVariable String name, Model model) {
        ProcessStatusRest proc = new ProcessStatusRest(name, processRepository.getRunningProcess(name));
        model.addAttribute("proc", proc);
        model.addAttribute("title", "Process Details");
        model.addAttribute("content", "proc/detail");
        if (link) {return "proc/detail";}
        return "layout";
    }

    @GetMapping("/log/{name}")
    public String processLog(@PathVariable String name,
                             @RequestParam(defaultValue = "stdout") String type,
                             @RequestParam(defaultValue = "100") int lines,
                             Model model) throws IOException {
        RunningProcess runningProcess = processRepository.getRunningProcess(name);
        String stdoutContents = "";
        if (runningProcess != null 
                && runningProcess.getStdout() != null 
                && runningProcess.getStdout().exists()) {

            stdoutContents = readLastLines(runningProcess.getStdout().toPath(), lines);
        }
        ProcessStatusRest proc = new ProcessStatusRest(name, processRepository.getRunningProcess(name));
        model.addAttribute("pr", proc);
        model.addAttribute("stdoutContents", stdoutContents);
        model.addAttribute("title", "Process Log");
        model.addAttribute("content", "proc/process-log");
        return "layout";
    }

    private String readLastLines(java.nio.file.Path path, int lines) throws IOException {
        String content = Files.readString(path);
        String[] allLines = content.split("\n");
        if (allLines.length <= lines) {
            return content;
        }
        return String.join("\n",
                java.util.Arrays.copyOfRange(allLines, allLines.length - lines, allLines.length));
    }

    @GetMapping("/events")
    public String getEvents(Model model) throws IOException {
        model.addAttribute("events", eventRepository.findAll());
        model.addAttribute("title", "Events");
        model.addAttribute("content", "proc/events");
        return "layout";
//        return "proc/events";
    }

}