package com.example.Entities;

public record Candidate(
        Integer userId,
        Integer candidateId,
        String resumeLinkPath,
        Integer experience,
        String education,
        String skills
) {
}
