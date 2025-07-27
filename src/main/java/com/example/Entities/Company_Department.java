package com.example.Entities;

public record Company_Department(
        Integer company_dept_id,
        Integer dept_id,
        Integer company_id,
        String description,
        Integer total_offers,
        Integer total_accepted_offers
) {
}
