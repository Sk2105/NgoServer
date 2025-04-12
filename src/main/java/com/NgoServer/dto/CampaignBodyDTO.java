package com.NgoServer.dto;

import com.NgoServer.utils.CampaignStatus;

public record CampaignBodyDTO(
    String title,
    String description,
    Double goalAmount,
    Double collectedAmount,
    CampaignStatus status
) {

   
    
}
