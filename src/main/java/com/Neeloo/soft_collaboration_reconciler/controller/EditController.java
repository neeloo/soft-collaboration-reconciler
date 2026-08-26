package com.Neeloo.soft_collaboration_reconciler.controller;

import com.Neeloo.soft_collaboration_reconciler.Dtos.EditRequest;
import com.Neeloo.soft_collaboration_reconciler.Service.ReconciliationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EditController {

    private final ReconciliationService reconciliationService;

    @PostMapping("/{id}/edits")
    public String submitEdit(
            @PathVariable Long id,
            @Valid @RequestBody EditRequest request
    ) {

        return reconciliationService.submitEdit(
                id,
                request
        );
    }
}