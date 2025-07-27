package com.example.Entities;

import java.time.LocalDateTime;

public record Application(
        Integer applicationId,
        Integer candidateId,
        Integer jobId,
        Integer currentStageId,
        LocalDateTime appliedAt
) {
}
