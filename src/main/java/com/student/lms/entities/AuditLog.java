package com.student.lms.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // User affected by the action
    private String performedBy;   // Who performed the action (email or system)
    private String action;        // CREATE, UPDATE, DELETE, ROLE_CHANGE, LOGIN_ATTEMPT, etc.
    private String entity;        // e.g., "User"
    private String details;       // Extra descriptive info

    private LocalDateTime timestamp = LocalDateTime.now();
}
