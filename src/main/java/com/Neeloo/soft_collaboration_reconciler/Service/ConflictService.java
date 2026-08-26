package com.Neeloo.soft_collaboration_reconciler.Service;


import com.Neeloo.soft_collaboration_reconciler.Dtos.ResolveConflictRequest;
import com.Neeloo.soft_collaboration_reconciler.Entity.Conflict;
import com.Neeloo.soft_collaboration_reconciler.Entity.Document;
import com.Neeloo.soft_collaboration_reconciler.Entity.PendingEdit;
import com.Neeloo.soft_collaboration_reconciler.Repository.ConflictRepository;
import com.Neeloo.soft_collaboration_reconciler.Repository.DocumentRepository;
import com.Neeloo.soft_collaboration_reconciler.Repository.PendingEditRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConflictService {

    private final ConflictRepository conflictRepository;
    private final PendingEditRepository pendingRepository;
    private final DocumentRepository documentRepository;

    public List<Conflict> getOpenConflicts(
            Long documentId
    ) {

        return conflictRepository
                .findByDocumentIdAndStatus(
                        documentId,
                        "OPEN"
                );
    }

    @Transactional
    public Conflict resolve(
            Long conflictId,
            ResolveConflictRequest request
    ) {

        Conflict conflict =
                conflictRepository.findById(conflictId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Conflict not found"
                                ));

        if (!"OPEN".equals(conflict.getStatus())) {
            throw new RuntimeException(
                    "Conflict already resolved"
            );
        }

        Document document =
                documentRepository.findById(
                        conflict.getDocumentId()
                ).orElseThrow();

        PendingEdit local =
                pendingRepository.findById(
                        conflict.getLocalEditId()
                ).orElseThrow();

        if ("ACCEPT_LOCAL".equals(
                request.getDecision())) {

            String oldLine =
                    conflict.getFieldName()
                            + ": "
                            + conflict.getRemoteValue();

            String newLine =
                    conflict.getFieldName()
                            + ": "
                            + conflict.getLocalValue();

            document.setContent(
                    document.getContent()
                            .replace(oldLine, newLine)
            );

            local.setStatus("APPLIED");

            conflict.setResolution(
                    "ACCEPT_LOCAL"
            );

        } else if ("ACCEPT_REMOTE".equals(
                request.getDecision())) {

            local.setStatus("REJECTED");

            conflict.setResolution(
                    "ACCEPT_REMOTE"
            );

        } else {

            throw new RuntimeException(
                    "Invalid decision"
            );
        }

        document.setVersion(
                document.getVersion() + 1
        );

        documentRepository.save(document);
        pendingRepository.save(local);

        conflict.setStatus("RESOLVED");
        conflict.setResolvedAt(
                LocalDateTime.now()
        );

        return conflictRepository.save(conflict);
    }
}