package com.example.Entities;

import com.example.Enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(
        Integer userId,
        String name,
        String email,
        Gender gender,
        String phoneNo,
        LocalDate dateOfBirth,
        LocalDateTime createdAt

) {
}
