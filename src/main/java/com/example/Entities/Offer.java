package com.example.Entities;

import com.example.Enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Offer(
        Integer offerId,
        int applicationId,
        BigDecimal salary,
        LocalDateTime issuedAt,
        LocalDateTime validTill,
        OfferStatus status,
        String description
) {
}
