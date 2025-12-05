package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.model.RunningProcess;
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
@RequestMapping("/log")
@RequiredArgsConstructor
public class WebLogController {

    /**
     * Repository for managing process entities
     */
    private final ProcessRepository processRepository;

    /**
     * Flag indicating whether to return direct page templates
     */
    private final boolean linkToPage = false;


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
    @GetMapping("/{name}")
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
        ProcessStatusInfo proc = new ProcessStatusInfo(name, processRepository.getRunningProcess(name));

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