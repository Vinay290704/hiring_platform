package com.example.Entities;

import com.example.Enums.InterviewResult;
import com.example.Enums.InterviewStatus;

import java.time.LocalDateTime;

public record Interview(
        Integer id,
        int applicationId,
        int interviewerId,
        LocalDateTime scheduledAt,
        String feedback,
        InterviewStatus status,
        InterviewResult result
) {
}
