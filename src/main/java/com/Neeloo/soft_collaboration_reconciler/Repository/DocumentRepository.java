package com.Neeloo.soft_collaboration_reconciler.Repository;


import com.Neeloo.soft_collaboration_reconciler.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {
}