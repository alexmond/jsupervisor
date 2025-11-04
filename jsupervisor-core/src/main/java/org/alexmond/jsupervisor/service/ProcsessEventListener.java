package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEvent;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class ProcsessEventListener {
    private final EventRepository eventRepository;

    @EventListener
    public void handleUserRegisteredEvent(ProcessEvent processEvent) {
        log.info("Handle process event for {}, with new status {}"
                ,processEvent.getProcessName()
                ,processEvent.getNewStatus());
                eventRepository.save(processEvent);
    }
}
