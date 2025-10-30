package org.alexmond.jsupervisor;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.ProcessManager;
import org.alexmond.jsupervisor.service.ProcessManagerBulk;
import org.alexmond.jsupervisor.service.ProcessManagerMonitor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@AutoConfiguration
@ConditionalOnClass({ProcessManager.class, SupervisorConfig.class})
//@ConditionalOnProperty(prefix = "jsupervisor", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SupervisorConfig.class)
@ComponentScan(basePackages = "org.alexmond.jsupervisor")
@EnableAsync
public class JSupervisorAutoConfiguration {

    /**
     * Creates a ThreadPoolTaskScheduler bean if none exists.
     * This is used for scheduling health checks and other async operations.
     */
    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskScheduler.class)
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("jsupervisor-");
        scheduler.initialize();
        return scheduler;
    }

    /**
     * Creates an EventRepository bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(EventRepository.class)
    public EventRepository eventRepository() {
        return new EventRepository();
    }

    /**
     * Creates a ProcessRepository bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessRepository.class)
    public ProcessRepository processRepository(SupervisorConfig supervisorConfig, EventRepository eventRepository) {
        return new ProcessRepository(supervisorConfig, eventRepository);
    }

    /**
     * Creates a ProcessManagerMonitor bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessManagerMonitor.class)
    public ProcessManagerMonitor processManagerMonitor(ProcessRepository processRepository,EventRepository eventRepository) {
        return new ProcessManagerMonitor(processRepository,eventRepository);
    }

    /**
     * Creates a ProcessManager bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessManager.class)
    public ProcessManager processManager(SupervisorConfig supervisorConfig,
                                         ProcessRepository processRepository,
                                         ProcessManagerMonitor processManagerMonitor,
                                         ThreadPoolTaskScheduler threadPoolTaskScheduler,
                                         EventRepository eventRepository) {
        return new ProcessManager(supervisorConfig, processRepository, processManagerMonitor,
                threadPoolTaskScheduler, eventRepository);
    }

    /**
     * Creates a ProcessManagerBulk bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessManagerBulk.class)
    public ProcessManagerBulk processManagerBulk(ProcessRepository processRepository,
                                                 ProcessManager processManager) {
        return new ProcessManagerBulk(processRepository, processManager);
    }
}