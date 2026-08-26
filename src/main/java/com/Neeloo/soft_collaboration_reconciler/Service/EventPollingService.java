package com.Neeloo.soft_collaboration_reconciler.Service;

import com.Neeloo.soft_collaboration_reconciler.Entity.DocumentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPollingService {

    private final EventService eventService;

    /*
     * Demo for  writer hard code to check.
     */
    private final String writerId = "WRITER-A";

    private Long lastVersion = 0L;

    //    @Scheduled(fixedDelay = 2000)
    public void poll() {
        Long documentId = 1L;
        List<DocumentEvent> events = eventService.getEvents(documentId, lastVersion);
        for (DocumentEvent event : events) {
            if (!writerId.equals(event.getWriterId())) {
                System.out.println("Incoming remote event: " + event.getId());
                System.out.println("Writer: "+ event.getWriterId());
                System.out.println(event.getFieldName() + " -> " + event.getNewValue());
            }
            lastVersion = Math.max(lastVersion, event.getNewVersion());
        }
    }
}