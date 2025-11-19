package com.student.lms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MFASecret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Base32 encoded secret key
    private String secretKey;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
