package com.Neeloo.soft_collaboration_reconciler.Service;


import com.Neeloo.soft_collaboration_reconciler.Dtos.CreateDocumentRequest;
import com.Neeloo.soft_collaboration_reconciler.Entity.Document;
import com.Neeloo.soft_collaboration_reconciler.Repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document create(CreateDocumentRequest request) {

        Document document = new Document();

        document.setName(request.getName());
        document.setContent(
                request.getContent() == null
                        ? ""
                        : request.getContent()
        );

        document.setVersion(0L);

        return documentRepository.save(document);
    }

    public Document get(Long id) {

        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));
    }

    public List<Document> getAll() {

        return documentRepository.findAll();
    }
}