package com.Neeloo.soft_collaboration_reconciler.controller;

import com.Neeloo.soft_collaboration_reconciler.Entity.DocumentEvent;
import com.Neeloo.soft_collaboration_reconciler.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}/events")
    public List<DocumentEvent> getEvents(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0")
            Long afterVersion
    ) {

        return eventService.getEvents(
                id,
                afterVersion
        );
    }
}