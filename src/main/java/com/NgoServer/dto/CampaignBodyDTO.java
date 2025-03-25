package com.NgoServer.dto;

import com.NgoServer.utils.CampaignStatus;

public record CampaignBodyDTO(
    String title,
    String description,
    Double goalAmount,
    CampaignStatus status
) {

   
    
}
