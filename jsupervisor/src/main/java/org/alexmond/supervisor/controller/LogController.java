package org.alexmond.supervisor.controller;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.model.ProcessStatusRest;
import org.alexmond.supervisor.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/logs")
@Slf4j
public class LogController {
    
    private final ProcessRepository processRepository;
    
    @Autowired
    public LogController(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }
    
    @GetMapping
    public String logsPage(Model model) {
        model.addAttribute("processes", processRepository.findAllProcessStatusRest());
        return "logs/index";
    }
    
    @GetMapping("/{processName}")
    public String viewLogs(@PathVariable String processName, 
                          @RequestParam(defaultValue = "stdout") String type,
                          @RequestParam(defaultValue = "100") int lines,
                          Model model) {
        
        var processInfo = processRepository.getRunningProcess(processName);
        if (processInfo == null) {
            model.addAttribute("error", "Process not found: " + processName);
            return "logs/error";
        }
        
        File logFile = "stderr".equals(type) ? processInfo.getStderr() : processInfo.getStdout();
        
        try {
            String content = "";
            if (logFile != null && logFile.exists()) {
                List<String> allLines = Files.readAllLines(logFile.toPath());
                int startLine = Math.max(0, allLines.size() - lines);
                List<String> lastLines = allLines.subList(startLine, allLines.size());
                content = String.join("\n", lastLines);
            }
            
            model.addAttribute("processName", processName);
            model.addAttribute("logType", type);
            model.addAttribute("logContent", content);
            model.addAttribute("logFile", logFile != null ? logFile.getName() : "N/A");
            model.addAttribute("processInfo", new ProcessStatusRest(processName,processInfo));
            model.addAttribute("lines", lines);
            
        } catch (IOException e) {
            log.error("Error reading log file for process: {}", processName, e);
            model.addAttribute("error", "Error reading log file: " + e.getMessage());
            return "logs/error";
        }
        
        return "logs/view";
    }
}
