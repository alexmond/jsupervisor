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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

/**
 * Controller handling web interface operations for process management.
 * Provides endpoints for viewing and managing processes, their logs, and events.
 */
@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebProcessController {

    /**
     * Repository for managing process entities
     */
    private final ProcessRepository processRepository;
    /**
     * Service for individual process management operations
     */
    private final ProcessManager processManager;
    /**
     * Service for bulk process management operations
     */
    private final ProcessManagerBulk processManagerBulk;
    /**
     * Configuration settings for the supervisor
     */
    private final SupervisorConfig supervisorConfig;
    /**
     * Repository for managing process events
     */
    private final EventRepository eventRepository;
    /**
     * Flag indicating whether to return direct page templates
     */
    private final boolean linkToPage = false;


    /**
     * Displays the main page with a list of all processes.
     *
     * @param model the Spring MVC model
     * @return the view name to render
     */
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

    /**
     * Initiates the start operation for a specified process.
     *
     * @param name the name of the process to start
     * @return redirect to the main page
     */
    @GetMapping("/start/{name}")
    public String startProcess(@PathVariable String name, RedirectAttributes redirectAttributes) {
        processManager.startProcess(name);
        redirectAttributes.addFlashAttribute("successMessage", "Process "+name+" started");
        return "redirect:/";
    }

    /**
     * Initiates the stop operation for a specified process.
     *
     * @param name the name of the process to stop
     * @return redirect to the main page
     */
    @GetMapping("/stop/{name}")
    public String stopProcess(@PathVariable String name, RedirectAttributes redirectAttributes) {
        processManager.stopProcess(name);
        redirectAttributes.addFlashAttribute("successMessage", "Process "+name+" stopped");
        return "redirect:/";
    }

    /**
     * Initiates the restart operation for a specified process.
     *
     * @param name the name of the process to restart
     * @return redirect to the main page
     */
    @GetMapping("/restart/{name}")
    public String restartProcess(@PathVariable String name, RedirectAttributes redirectAttributes) {
        processManager.restartProcess(name);
        redirectAttributes.addFlashAttribute("successMessage", "Process "+name+" restarted");
        return "redirect:/";
    }

    /**
     * Starts all configured processes.
     *
     * @return redirect to the main page
     * @throws IOException if there is an error starting the processes
     */
    @GetMapping("/startAll")
    public String startAll() throws IOException {
        processManagerBulk.startAll();
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
        processManagerBulk.stopAll();
        return "redirect:/";
    }

    @GetMapping("/restartAll")
    public String restartAll() throws IOException {
        processManagerBulk.restartAll();
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

    /**
     * Displays the event history page.
     *
     * @param model the Spring MVC model
     * @return the view name to render
     */
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