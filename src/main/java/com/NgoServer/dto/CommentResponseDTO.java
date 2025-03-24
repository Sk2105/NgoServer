package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.models.User;

public record CommentResponseDTO(
    Long id,
    String content,
    LocalDateTime createdAt,
    User author
) {
    
}
