package org.alexmond.supervisor.controller;

import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.model.RunningProcess;
import org.alexmond.supervisor.model.SupervisorRest;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebIndexController {

    private final ProcessRepository processRepository;
    private final SupervisorRest supervisorRest;

    public WebIndexController(ProcessRepository processRepository, SupervisorRest supervisorRest) {
        this.processRepository = processRepository;
        this.supervisorRest = supervisorRest;
    }

    @GetMapping
    public String listUProcesses(Model model) {
        List<SupervisorRest> supervisors = new ArrayList<>();
        supervisorRest.refresh();
        supervisors.add(supervisorRest);
        model.addAttribute("supervisors", supervisors);
        model.addAttribute("title", "==== Index List");
        model.addAttribute("message", "==== TEST MESSAGE");
        return "/index";
    }
}