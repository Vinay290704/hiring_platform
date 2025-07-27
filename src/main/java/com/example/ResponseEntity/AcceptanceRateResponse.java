package com.example.ResponseEntity;

public record AcceptanceRateResponse(
        Integer company_dept_id,
        Integer dept_id,
        Integer company_id,
        String companyName,
        String departmentName,
        Double acceptance_rate
) {

}
