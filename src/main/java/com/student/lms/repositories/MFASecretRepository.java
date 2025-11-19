package com.student.lms.repositories;

import com.student.lms.entities.MFASecret;
import com.student.lms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MFASecretRepository extends JpaRepository<MFASecret, Long> {
    Optional<MFASecret> findByUser(User user);
}
