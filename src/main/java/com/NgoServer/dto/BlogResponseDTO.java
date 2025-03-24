package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.models.User;

public record BlogResponseDTO(
    Long id,
    String title,
    String content,
    String image,
    LocalDateTime createdAt,
    LocalDateTime updateAt,
    User author
) {
    
}
