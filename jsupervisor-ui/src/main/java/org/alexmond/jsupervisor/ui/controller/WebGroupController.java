package org.alexmond.jsupervisor.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessStatusInfo;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessGroupManager;
import org.alexmond.jsupervisor.ui.model.GroupsPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

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

    private final ProcessRepository processRepository;

    /**
     * Flag indicating whether to return direct page templates
     */
    private final boolean linkToPage = false;

    @GetMapping("/list")
    public String listGroups(Model model) {
        Map<String, Collection<ProcessStatusInfo>> groups = new TreeMap<>();
        processRepository.getProcessGroups().keySet().forEach(group -> {
            groups.put(group, processRepository.findGroupProcessInfo(group));
        });

        GroupsPageModel pageModel = GroupsPageModel.builder()
                .title("Process Groups")
                .activePage("groups")
                .groups(groups)
                .build();

        model.addAttribute("pageModel", pageModel);
        model.addAttribute("content", "proc/groups");
        if (linkToPage) return "proc/groups";
        return "layout";
    }


    /**
     * Starts all configured processes.
     *
     * @return redirect to the main page
     * @throws IOException if there is an error starting the processes
     */
    @GetMapping("/start-all")
    public String startAll(RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.startAll();
        redirectAttributes.addFlashAttribute("successMessage", "All processes started");
        return "redirect:/group/list";
    }

    /**
     * Stops all running processes.
     *
     * @return redirect to the main page
     * @throws IOException if there is an error stopping the processes
     */
    @GetMapping("/stop-all")
    public String stopAll(RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.stopAll();
        redirectAttributes.addFlashAttribute("successMessage", "All processes stopped");
        return "redirect:/group/list";
    }

    @GetMapping("/restart-all")
    public String restartAll(RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.restartAll();
        redirectAttributes.addFlashAttribute("successMessage", "All processes restarted");
        return "redirect:/group/list";
    }

    @GetMapping("/start/{group}")
    public String startGroup(@PathVariable String group, RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.startGroup(group);
        redirectAttributes.addFlashAttribute("successMessage", "Group " + group + " started");
        return "redirect:/group/list";
    }

    @GetMapping("/stop/{group}")
    public String stopGroup(@PathVariable String group, RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.stopGroup(group);
        redirectAttributes.addFlashAttribute("successMessage", "Group " + group + " stopped");
        return "redirect:/group/list";
    }

    @GetMapping("/restart/{group}")
    public String restartGroup(@PathVariable String group, RedirectAttributes redirectAttributes) throws IOException {
        processGroupManager.restartGroup(group);
        redirectAttributes.addFlashAttribute("successMessage", "Group " + group + " restarted");
        return "redirect:/group/list";
    }
    

}