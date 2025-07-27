package com.example.ResponseEntity;

import com.example.Enums.JobStatus;

public record JobResponse(
        Integer job_id,
        String title,
        int total_applications,
        JobStatus status,
        Integer posted_by,
        String description
) {
}
