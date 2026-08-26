package com.Neeloo.soft_collaboration_reconciler.controller;

import com.Neeloo.soft_collaboration_reconciler.Dtos.ResolveConflictRequest;
import com.Neeloo.soft_collaboration_reconciler.Entity.Conflict;
import com.Neeloo.soft_collaboration_reconciler.Service.ConflictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conflicts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ConflictController {

    private final ConflictService conflictService;

    @GetMapping("/document/{documentId}")
    public List<Conflict> getConflicts(@PathVariable Long documentId) {

        return conflictService.getOpenConflicts(documentId);
    }

    @PostMapping("/{id}/resolve")
    public Conflict resolve(
            @PathVariable Long id,
            @Valid @RequestBody ResolveConflictRequest request
    ) {

        return conflictService.resolve(id, request);
    }
}