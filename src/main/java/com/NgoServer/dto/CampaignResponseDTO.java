package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.CampaignStatus;

public record CampaignResponseDTO(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    Double goalAmount,
    Double collectedAmount,
    CampaignStatus status
) {
    
}
