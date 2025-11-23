package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusRest;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.alexmond.jsupervisor.service.ProcessGroupManager;
import org.alexmond.jsupervisor.ui.model.ProcessDetailPageModel;
import org.alexmond.jsupervisor.ui.model.ProcessLogPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Controller handling web interface operations for process management.
 * Provides endpoints for viewing and managing processes, their logs, and events.
 */
@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebGroupController {

    /**
     * Repository for managing process entities
     */
    private final ProcessRepository processRepository;
    /**
     * Service for bulk process management operations
     */
    private final ProcessGroupManager processGroupManager;
    /**
     * Flag indicating whether to return direct page templates
     */
    private final boolean linkToPage = false;


    /**
     * Starts all configured processes.
     *
     * @return redirect to the main page
     * @throws IOException if there is an error starting the processes
     */
    @GetMapping("/startAll")
    public String startAll() throws IOException {
        processGroupManager.startAll();
        return "redirect:/";
    }

    /**
     * Stops all running processes.
     *
     * @return redirect to the main page
     * @throws IOException if there is an error stopping the processes
     */
    @GetMapping("/stopAll")
    public String stopAll() throws IOException {
        processGroupManager.stopAll();
        return "redirect:/";
    }

    @GetMapping("/restartAll")
    public String restartAll() throws IOException {
        processGroupManager.restartAll();
        return "redirect:/";
    }

    /**
     * Displays detailed information about a specific process.
     *
     * @param name  the name of the process
     * @param model the Spring MVC model
     * @return the view name to render
     */
    @GetMapping("/details/{name}")
    public String processesDetails(@PathVariable String name, Model model) {
        ProcessStatusRest proc = new ProcessStatusRest(name, processRepository.getRunningProcess(name));
        ProcessDetailPageModel pageModel = ProcessDetailPageModel.builder()
                .title("Process Details")
                .activePage("processes")
                .proc(proc)
                .processConfig(proc.getProcessConfig().toMap())
                .process(proc.toMap())
                .build();
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/detail");

        if (linkToPage) return "proc/detail";
        return "layout";
    }

    /**
     * Displays log content for a specific process.
     *
     * @param name  the name of the process
     * @param type  the type of log to display (stdout or stderr)
     * @param lines the number of lines to display
     * @param model the Spring MVC model
     * @return the view name to render
     * @throws IOException if there is an error reading the log file
     */
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
        File logFile = switch (type) {
            case "stderr" -> runningProcess.getStderr();
            case "stdout" -> runningProcess.getStdout();
            case "application" -> runningProcess.getApplication();
            default -> runningProcess.getStdout();
        };

        String logContent = "";
        try {
            logContent = Files.readString(logFile.toPath());
        } catch (IOException e) {
            log.error("Error reading log file", e);
            logContent = "Error reading log file: " + e.toString();
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

//    /**
//     * Displays the event history page.
//     *
//     * @param model the Spring MVC model
//     * @return the view name to render
//     */
//    @GetMapping("/events")
//    public String getEvents(Model model) {
//        EventsPageModel pageModel = EventsPageModel.builder()
//                .title("Events")
//                .activePage("events")
//                .events(eventRepository.findAll())
//                .build();
//        model.addAttribute("pageModel", pageModel);
//        model.addAttribute("content", "proc/events");
//        if (linkToPage) return "proc/events";
//        return "layout";
//    }
}