package com.example.ResponseEntity;

public record ApplicationStatusOfCandidate(
        Integer applicationId,
        Integer jobId,
        String status,
        String jobTitle,
        String appliedAt
) {
}
