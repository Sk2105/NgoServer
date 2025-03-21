package com.NgoServer.dto;

public record ChangePasswordDTO(
    String password,
    String newPassword
) {
    
}
