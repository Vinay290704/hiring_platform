package com.example.ResponseEntity;

public record InterviewSchedules(
        Integer applicationId,
        String name,
        String jobTitle,
        String scheduledAt,
        String resumeLinkPath
) {
}
