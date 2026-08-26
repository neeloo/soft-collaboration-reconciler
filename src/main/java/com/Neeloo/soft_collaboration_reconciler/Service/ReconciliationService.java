package com.Neeloo.soft_collaboration_reconciler.Service;

import com.Neeloo.soft_collaboration_reconciler.Dtos.EditRequest;
import com.Neeloo.soft_collaboration_reconciler.Entity.Conflict;
import com.Neeloo.soft_collaboration_reconciler.Entity.Document;
import com.Neeloo.soft_collaboration_reconciler.Entity.DocumentEvent;
import com.Neeloo.soft_collaboration_reconciler.Entity.PendingEdit;
import com.Neeloo.soft_collaboration_reconciler.Repository.ConflictRepository;
import com.Neeloo.soft_collaboration_reconciler.Repository.DocumentEventRepository;
import com.Neeloo.soft_collaboration_reconciler.Repository.DocumentRepository;
import com.Neeloo.soft_collaboration_reconciler.Repository.PendingEditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReconciliationService {

    private final DocumentRepository documentRepository;
    private final DocumentEventRepository eventRepository;
    private final PendingEditRepository pendingRepository;
    private final ConflictRepository conflictRepository;

    @Transactional
    public String submitEdit(
            Long documentId,
            EditRequest request
    ) {

        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        /*
         * Check stale version.
         */
        if (!request.getBaseVersion()
                .equals(document.getVersion())) {

            PendingEdit pending = createPending(
                    documentId,
                    request
            );

            List<DocumentEvent> remoteEvents =
                    eventRepository
                            .findByDocumentIdAndNewVersionGreaterThanOrderByNewVersionAsc(
                                    documentId,
                                    request.getBaseVersion()
                            );

            for (DocumentEvent remote : remoteEvents) {

                if (isConflict(request, remote)) {

                    pending.setStatus("CONFLICT");

                    pendingRepository.save(pending);

                    createConflict(
                            documentId,
                            pending,
                            remote
                    );

                    return "CONFLICT";
                }
            }

            /*
             * No conflict.
             * Rebase local edit on latest version.
             */
            applyEdit(document, request);

            document.setVersion(document.getVersion() + 1);

            documentRepository.save(document);

            createEvent(
                    document,
                    request
            );

            pending.setStatus("APPLIED");

            pendingRepository.save(pending);

            return "REBASSED";
        }

        /*
         * Normal edit.
         */
        applyEdit(document, request);

        document.setVersion(document.getVersion() + 1);

        documentRepository.save(document);

        createEvent(
                document,
                request
        );

        return "APPLIED";
    }

    private boolean isConflict(
            EditRequest local,
            DocumentEvent remote
    ) {

        if (!local.getFieldName()
                .equals(remote.getFieldName())) {

            return false;
        }

        /*
         * Same field but same resulting value
         * is not a real conflict.
         */
        return !safeEquals(
                local.getNewValue(),
                remote.getNewValue()
        );
    }

    private void applyEdit(
            Document document,
            EditRequest request
    ) {

        String content = document.getContent();

        String line =
                request.getFieldName()
                        + ": "
                        + request.getNewValue();

        String oldLine =
                request.getFieldName()
                        + ": "
                        + request.getOldValue();

        if (content.contains(oldLine)) {

            content = content.replace(
                    oldLine,
                    line
            );

        } else {

            content += "\n" + line;
        }

        document.setContent(content);
    }

    private PendingEdit createPending(
            Long documentId,
            EditRequest request
    ) {

        PendingEdit pending = new PendingEdit();

        pending.setDocumentId(documentId);
        pending.setWriterId(request.getWriterId());
        pending.setBaseVersion(request.getBaseVersion());
        pending.setFieldName(request.getFieldName());
        pending.setOldValue(request.getOldValue());
        pending.setNewValue(request.getNewValue());
        pending.setStatus("PENDING");

        return pendingRepository.save(pending);
    }

    private void createEvent(
            Document document,
            EditRequest request
    ) {

        DocumentEvent event = new DocumentEvent();

        event.setDocumentId(document.getId());
        event.setWriterId(request.getWriterId());

        event.setBaseVersion(
                document.getVersion() - 1
        );

        event.setNewVersion(
                document.getVersion()
        );

        event.setFieldName(
                request.getFieldName()
        );

        event.setOldValue(
                request.getOldValue()
        );

        event.setNewValue(
                request.getNewValue()
        );

        eventRepository.save(event);
    }

    private void createConflict(
            Long documentId,
            PendingEdit local,
            DocumentEvent remote
    ) {

        Conflict conflict = new Conflict();

        conflict.setDocumentId(documentId);

        conflict.setLocalEditId(
                local.getId()
        );

        conflict.setRemoteEventId(
                remote.getId()
        );

        conflict.setFieldName(
                local.getFieldName()
        );

        conflict.setLocalValue(
                local.getNewValue()
        );

        conflict.setRemoteValue(
                remote.getNewValue()
        );

        conflict.setStatus("OPEN");

        conflictRepository.save(conflict);
    }

    private boolean safeEquals(
            String a,
            String b
    ) {

        if (a == null) {
            return b == null;
        }

        return a.equals(b);
    }
}