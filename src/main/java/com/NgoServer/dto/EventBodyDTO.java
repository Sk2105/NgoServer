package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.EventStatus;

public record EventBodyDTO(
    String title,
    String description,
    String image,
    String location,
    LocalDateTime startDate,
    LocalDateTime endDate,
    EventStatus status
) {
}
