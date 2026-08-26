package com.Neeloo.soft_collaboration_reconciler.controller;

import com.Neeloo.soft_collaboration_reconciler.Dtos.CreateDocumentRequest;
import com.Neeloo.soft_collaboration_reconciler.Entity.Document;
import com.Neeloo.soft_collaboration_reconciler.Service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor

public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public Document create(@Valid @RequestBody CreateDocumentRequest request) {
        return documentService.create(request);
    }

    @GetMapping("/{id}")
    public Document get(@PathVariable Long id) {
        return documentService.get(id);
    }

    @GetMapping
    public List<Document> getAll() {
        return documentService.getAll();
    }
}