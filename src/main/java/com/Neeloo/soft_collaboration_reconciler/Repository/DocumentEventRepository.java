package com.Neeloo.soft_collaboration_reconciler.Repository;


import com.Neeloo.soft_collaboration_reconciler.Entity.DocumentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentEventRepository
        extends JpaRepository<DocumentEvent, Long> {

    List<DocumentEvent> findByDocumentIdAndNewVersionGreaterThanOrderByNewVersionAsc(
            Long documentId,
            Long version
    );
}