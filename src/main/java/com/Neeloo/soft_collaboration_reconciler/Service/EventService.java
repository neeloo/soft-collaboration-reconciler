package com.Neeloo.soft_collaboration_reconciler.Service;


import com.Neeloo.soft_collaboration_reconciler.Entity.DocumentEvent;
import com.Neeloo.soft_collaboration_reconciler.Repository.DocumentEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final DocumentEventRepository eventRepository;

    public List<DocumentEvent> getEvents(
            Long documentId,
            Long afterVersion
    ) {

        return eventRepository
                .findByDocumentIdAndNewVersionGreaterThanOrderByNewVersionAsc(
                        documentId,
                        afterVersion
                );
    }

    public List<DocumentEvent> getAll(Long documentId) {

        return eventRepository
                .findByDocumentIdAndNewVersionGreaterThanOrderByNewVersionAsc(
                        documentId,
                        -1L
                );
    }
}