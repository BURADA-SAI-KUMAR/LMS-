package com.student.lms.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.lms.Model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	 Optional<Student> findByEmail(String email);

	    boolean existsByEmail(String email);

		Student findByFirstName(String firstName);
}
