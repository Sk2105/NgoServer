package com.NgoServer.dto;

import com.NgoServer.utils.CampaignStatus;

public record CampaignDTO(
    String title,
    String description,
    Double goalAmount,
    Double collectedAmount,
    CampaignStatus status
) {

   
    
}
