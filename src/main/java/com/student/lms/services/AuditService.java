package com.student.lms.services;

import com.student.lms.entities.AuditLog;
import com.student.lms.entities.User;
import com.student.lms.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(User targetUser, String action, String entity, String details) {

        AuditLog log = AuditLog.builder()
                .userId(targetUser != null ? targetUser.getId() : null)
                .performedBy("SYSTEM") // Replace this later with authenticated user
                .action(action)
                .entity(entity)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}
