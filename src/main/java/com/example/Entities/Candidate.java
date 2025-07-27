package com.example.Entities;

public record Candidate(
        Integer userId,
        Integer candidateId,
        String resumeLinkPath,
        Integer experience,
        String education,
        String skills
        // A single candidate may have different skills , we do can create a skill table and have candidate to skill many-to-many relationship too
) {
}
