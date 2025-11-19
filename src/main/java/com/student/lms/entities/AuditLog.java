package com.student.lms.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorId; // user performing the action

    private String action; // e.g., UPDATE_ROLE, DELETE_USER

    private String targetType; // e.g., USER
    private Long targetId;

    private LocalDateTime timestamp = LocalDateTime.now();

    // Additional information (JSON)
    @Column(columnDefinition = "TEXT")
    private String metadata;
}
