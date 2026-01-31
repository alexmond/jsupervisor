package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.ui.model.ProcessDetailPageModel;
import org.alexmond.jsupervisor.ui.model.ProcessesPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * Service for individual process management operations
     */
    private final ProcessManager processManager;
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
    public String getAllProcessesInfo(Model model) {
        Collection<ProcessStatusInfo> processes = processManager.getAllProcessStatusInfo();
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
        redirectAttributes.addFlashAttribute("successMessage", "Process " + name + " started");
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
        redirectAttributes.addFlashAttribute("successMessage", "Process " + name + " stopped");
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
        redirectAttributes.addFlashAttribute("successMessage", "Process " + name + " restarted");
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
    public String processesDetails(@PathVariable String name, 
                                   @RequestHeader(value = "Referer", required = false) String referer,
                                   Model model) {
        ProcessStatusInfo proc = processManager.getRunningProcessInfo(name);
        
        String backUrl = "/";
        String activePage = "processes";
        if (referer != null && referer.contains("/group/list")) {
            backUrl = "/group/list";
            activePage = "groups";
        }
        
        ProcessDetailPageModel pageModel = ProcessDetailPageModel.builder()
                .title("Process Details")
                .activePage(activePage)
                .proc(proc)
                .processConfig(proc.getProcessConfig().toMap())
                .process(proc.toMap())
                .backUrl(backUrl)
                .build();
        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/detail");

        if (linkToPage) return "proc/detail";
        return "layout";
    }

}