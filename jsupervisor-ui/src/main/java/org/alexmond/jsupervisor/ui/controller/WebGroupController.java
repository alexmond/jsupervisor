package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.service.ProcessGroupManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Controller handling web interface operations for process management.
 * Provides endpoints for viewing and managing processes, their logs, and events.
 */
@Slf4j
@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
public class WebGroupController {
    
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
    @GetMapping("/start-all")
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
    @GetMapping("/stop-all")
    public String stopAll() throws IOException {
        processGroupManager.stopAll();
        return "redirect:/";
    }

    @GetMapping("/restart-all")
    public String restartAll() throws IOException {
        processGroupManager.restartAll();
        return "redirect:/";
    }

    @GetMapping("/start/{group}")
    public String startGroup(@PathVariable String group) throws IOException {
        processGroupManager.startGroup(group);
        return "redirect:/";
    }

    @GetMapping("/stop/{group}")
    public String stopGroup(@PathVariable String group) throws IOException {
        processGroupManager.stopGroup(group);
        return "redirect:/";
    }

    @GetMapping("/restart/{group}")
    public String restartGroup(@PathVariable String group) throws IOException {
        processGroupManager.restartGroup(group);
        return "redirect:/";
    }
    

}