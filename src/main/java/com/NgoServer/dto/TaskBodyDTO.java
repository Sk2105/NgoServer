package com.NgoServer.dto;

import java.time.LocalDateTime;

public record TaskBodyDTO(
    String title,
    String description,
    String startAt,
    String endAt
) {
    
}
