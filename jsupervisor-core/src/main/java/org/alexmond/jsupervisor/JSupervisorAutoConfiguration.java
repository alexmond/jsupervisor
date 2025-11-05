package org.alexmond.jsupervisor;

import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.service.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@AutoConfiguration
@ConditionalOnClass({ProcessManager.class, SupervisorConfig.class})
//@ConditionalOnProperty(prefix = "jsupervisor", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SupervisorConfig.class)
@ComponentScan(basePackages = "org.alexmond.jsupervisor")
@EnableAsync
@EnableMapRepositories(basePackages = "org.alexmond.jsupervisor.repository")
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

//    /**
//     * Creates an EventRepository bean if none exists.
//     */
//    @Bean
//    @ConditionalOnMissingBean(EventRepository.class)
//    public EventRepository eventRepository() {
//        return new EventRepository();
//    }

    /**
     * Creates a ProcessRepository bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessRepository.class)
    public ProcessRepository processRepository(SupervisorConfig supervisorConfig, ApplicationEventPublisher eventPublisher) {
        return new ProcessRepository(supervisorConfig, eventPublisher);
    }

    /**
     * Creates a ProcessManagerMonitor bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessManagerMonitor.class)
    public ProcessManagerMonitor processManagerMonitor(ProcessRepository processRepository) {
        return new ProcessManagerMonitor(processRepository);
    }

    /**
     * Creates a ProcessManager bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(ProcessManager.class)
    public ProcessManager processManager(SupervisorConfig supervisorConfig,
                                         ProcessRepository processRepository,
                                         ProcessManagerMonitor processManagerMonitor,
                                         ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        return new ProcessManager(supervisorConfig, processRepository, processManagerMonitor,
                threadPoolTaskScheduler);
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

    /**
     * Creates a ProcessManagerBulk bean if none exists.
     */
    @Bean
    public ProcsessEventListener procsessEventListener(EventRepository eventRepository) {
        return new ProcsessEventListener(eventRepository);
    }

    /**
     * Creates a JSupervisorStartupManager bean if none exists.
     */
    @Bean
    @ConditionalOnMissingBean(JSupervisorStartupManager.class)
    public JSupervisorStartupManager jSupervisorStartupManager(SupervisorConfig supervisorConfig,
                                                               ProcessManagerBulk processManagerBulk) {
        return new JSupervisorStartupManager(supervisorConfig, processManagerBulk);
    }


}