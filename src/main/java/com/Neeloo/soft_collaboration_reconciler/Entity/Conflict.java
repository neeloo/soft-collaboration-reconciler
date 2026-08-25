package com.Neeloo.soft_collaboration_reconciler.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "conflicts")
@Getter
@Setter
public class Conflict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    private Long localEditId;

    private Long remoteEventId;

    private String fieldName;

    @Column(columnDefinition = "TEXT")
    private String localValue;

    @Column(columnDefinition = "TEXT")
    private String remoteValue;

    private String status;

    private String resolution;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = "OPEN";
        }
    }
}