package com.Neeloo.soft_collaboration_reconciler.Repository;


import com.Neeloo.soft_collaboration_reconciler.Entity.Conflict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConflictRepository
        extends JpaRepository<Conflict, Long> {

    List<Conflict> findByDocumentIdAndStatus(
            Long documentId,
            String status
    );
}