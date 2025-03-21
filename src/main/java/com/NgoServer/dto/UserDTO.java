package com.NgoServer.dto;


import com.NgoServer.utils.Role;

public record UserDTO(
    String username,
    String email,
    Role role,
    String password,
    String phoneNumber
) {

  
    
}
