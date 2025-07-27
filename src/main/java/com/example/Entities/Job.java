package com.example.Entities;

import com.example.Enums.JobStatus;

import java.time.LocalDateTime;

public record Job(
        Integer jobId,
        String title,
        int companyDeptId,
        String description,
        int postedBy,
        JobStatus status,
        LocalDateTime createdAt
) {
}
