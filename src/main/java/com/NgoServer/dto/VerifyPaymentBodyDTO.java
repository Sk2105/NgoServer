package com.NgoServer.dto;

public record VerifyPaymentBodyDTO(
    String paymentId,
    String orderId,
    String signature
) {
    
}
