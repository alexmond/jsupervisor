package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.model.ProcessEvent;
import org.alexmond.jsupervisor.model.ProcessEventEntry;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class ProcsessEventListener {
    private final EventRepository eventRepository;
    private final AtomicLong idGenerator = new AtomicLong(1);

    @EventListener
    public void handleUserRegisteredEvent(ProcessEvent processEvent) {
        log.info("Handle process event for {}, with new status {}",
                processEvent.getEntry().getProcessName(),
                processEvent.getEntry().getNewStatus());
        ProcessEventEntry processEventEntry = processEvent.getEntry();
        processEventEntry.setId(idGenerator.incrementAndGet());
        eventRepository.save(processEventEntry);
    }
}
