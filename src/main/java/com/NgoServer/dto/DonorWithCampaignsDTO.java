package com.NgoServer.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.NgoServer.models.User;

public record DonorWithCampaignsDTO(
    long id,
    User user,
    double totalDonation,
    LocalDateTime lastDonation,
    List<CampaignDTO> campaigns
) {
    

   
}
