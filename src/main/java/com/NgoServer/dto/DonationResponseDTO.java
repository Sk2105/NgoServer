package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.PaymentStatus;

public record DonationResponseDTO(
        Long id,
        Double amount,
        LocalDateTime createdAt,
        DonorDTO donor,
        String paymentId,
        PaymentStatus status,
        CampaignResponseDTO campaign) {

}
