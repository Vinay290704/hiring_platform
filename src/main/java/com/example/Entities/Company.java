package com.example.Entities;

import java.time.LocalDateTime;

public record Company(
        Integer companyId,
        String name,
        String email,
        String phoneNo,
        String address,
        LocalDateTime createdAt
) {
}
