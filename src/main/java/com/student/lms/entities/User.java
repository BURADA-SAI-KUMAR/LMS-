package com.student.lms.entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String firstName;
    
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private boolean emailVerified = false;

    private String status = "ACTIVE"; // ACTIVE, SUSPENDED, DELETED


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;


    
    private boolean mfaEnabled = false;   // enables MFA requirement
    private boolean mfaVerified = false;  // true when user finishes setup

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void setUpdateTimestamp() {
        updatedAt = LocalDateTime.now();
    }
}

