package com.student.lms.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(length = 20)
    private String phoneNumber;

    private String address;


    private LocalDate registeredDate;
    
    @Column(nullable = false)
    private String password;  // encrypted

    @Column(nullable = false)
    private String role; // e.g., STUDENT, ADMIN

  

}

