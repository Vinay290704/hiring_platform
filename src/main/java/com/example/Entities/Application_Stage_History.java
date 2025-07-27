package com.example.Entities;

import java.time.LocalDateTime;

public record Application_Stage_History(
        Integer id,
        Integer applicationId,
        Integer stage_id,
        Integer changed_by,
        LocalDateTime changed_at,
        String note
) {
}
