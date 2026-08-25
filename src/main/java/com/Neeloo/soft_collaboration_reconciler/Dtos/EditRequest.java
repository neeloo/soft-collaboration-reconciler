package com.Neeloo.soft_collaboration_reconciler.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRequest {

    @NotBlank
    private String writerId;

    @NotNull
    private Long baseVersion;

    @NotBlank
    private String fieldName;

    private String oldValue;

    private String newValue;
}