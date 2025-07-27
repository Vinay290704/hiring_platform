package com.example.Entities;

import com.example.Enums.JobStatus;

import java.time.LocalDateTime;

public record Job(
        Integer jobId,
        String title,
        Integer companyDeptId,
        String description,
        Integer postedBy,
        JobStatus status,
        LocalDateTime createdAt
) {
}
