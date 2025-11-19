package com.student.lms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SUPER_ADMIN, ADMIN, INSTRUCTOR, STUDENT, MANAGER, GUEST
    @Column(unique = true, nullable = false)
    private String name;

    // Hierarchy level: 0 = super admin, 1 = admin, 2 = instructor/manager, 3 = student, 4 = guest
    private Integer level;
}

