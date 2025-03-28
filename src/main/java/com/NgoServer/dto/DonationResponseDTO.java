package com.NgoServer.dto;

import java.time.LocalDateTime;

import com.NgoServer.utils.PaymentStatus;

public record DonationResponseDTO(
        Long id,
        Double amount,
        LocalDateTime createdAt,
        String paymentId,
        String orderId,
        String signature,
        PaymentStatus status
) {

}
