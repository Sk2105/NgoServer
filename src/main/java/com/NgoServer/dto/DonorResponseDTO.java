package com.NgoServer.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.NgoServer.models.User;

public record DonorResponseDTO(
        Long id,
        double totalDonation,
        LocalDateTime lastDonation,
        User user,
        List<DonationResponseDTO> donations) {

}
