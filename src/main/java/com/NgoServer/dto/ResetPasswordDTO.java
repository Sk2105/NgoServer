package com.NgoServer.dto;

public record ResetPasswordDTO(
    int otp,
    String password
) {
    
}
