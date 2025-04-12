package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.TaskStatus;

public record TaskResponseDTO(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime createdAt,
    LocalDateTime startAt,
    LocalDateTime endAt
) {
    
}
