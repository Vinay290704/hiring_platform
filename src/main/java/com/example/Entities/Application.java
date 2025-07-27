package com.example.Entities;

import java.time.LocalDateTime;

public record Application(
        Integer applicationId,
        int candidateId,
        int jobId,
        int currentStageId,
        LocalDateTime appliedAt
) {
}
