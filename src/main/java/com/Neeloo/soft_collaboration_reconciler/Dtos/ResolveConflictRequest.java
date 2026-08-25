package com.Neeloo.soft_collaboration_reconciler.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResolveConflictRequest {

    @NotBlank
    private String decision;
}