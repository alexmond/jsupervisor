package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.alexmond.jsupervisor.ui.model.EventsPageModel;
import org.alexmond.jsupervisor.ui.model.ProcessDetailPageModel;
import org.alexmond.jsupervisor.ui.model.ProcessLogPageModel;
import org.alexmond.jsupervisor.ui.model.ProcessesPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
    private final boolean linkToPage = false;


    @GetMapping({"/", "/index"})
    public String getAllProcesses(Model model) {
        Collection<ProcessStatusRest> processes = processRepository.findAllProcessStatusRest();
        ProcessesPageModel pageModel = ProcessesPageModel.builder()
                .title("Process List")
                .activePage("processes")
                .processes(processes)
                .build();
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/list");
        if (linkToPage) return "proc/list";
        return "layout";
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
        ProcessDetailPageModel pageModel = ProcessDetailPageModel.builder()
                .title("Process Details")
                .activePage("processes")
                .proc(proc)
                .build();
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/detail");
        if (linkToPage) return "proc/detail";
        return "layout";
    }

    @GetMapping("/log/{name}")
    public String processLog(@PathVariable String name,
                             @RequestParam(defaultValue = "stdout") String type,
                             @RequestParam(defaultValue = "100") int lines,
                             Model model) throws IOException {

        RunningProcess runningProcess = processRepository.getRunningProcess(name);
        if (runningProcess == null) {
            model.addAttribute("error", "Process not found: " + name);
            return "logs/error";
        }
        File logFile = "stderr".equals(type) ? runningProcess.getStderr() : runningProcess.getStdout();

        String logContent = "";
        if (runningProcess.getStdout() != null && runningProcess.getStdout().exists()) {
            logContent = Files.readString(runningProcess.getStdout().toPath());
        }
        ProcessStatusRest proc = new ProcessStatusRest(name, processRepository.getRunningProcess(name));

        ProcessLogPageModel pageModel = ProcessLogPageModel.builder()
                .title("Process Log")
                .activePage("processes")
                .logType(type)
                .logFile(logFile != null ? logFile.getName() : "N/A")
                .lines(lines)
                .logContent(logContent)
                .pr(proc)
                .build();

        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/process-log");
        if (linkToPage) return "proc/process-log";
        return "layout";
    }

    @GetMapping("/events")
    public String getEvents(Model model) {
        EventsPageModel pageModel = EventsPageModel.builder()
                .title("Events")
                .activePage("events")
                .events(eventRepository.findAll())
                .build();
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/events");
        if (linkToPage) return "proc/events";
        return "layout";
    }
}