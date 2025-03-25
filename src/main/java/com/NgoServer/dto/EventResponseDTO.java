package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.EventStatus;

public record EventResponseDTO(
        Long id,
        String title,
        String description,
        String image,
        String location,
        LocalDateTime startDate,
        LocalDateTime endDate,
        EventStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        
        ) {

}
