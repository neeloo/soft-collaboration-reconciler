package com.Neeloo.soft_collaboration_reconciler.Repository;


import com.Neeloo.soft_collaboration_reconciler.Entity.PendingEdit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingEditRepository
        extends JpaRepository<PendingEdit, Long> {

    List<PendingEdit> findByDocumentIdAndWriterIdAndStatus(
            Long documentId,
            String writerId,
            String status
    );
}