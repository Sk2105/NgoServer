package com.NgoServer.dto;

import java.time.LocalDateTime;
import com.NgoServer.models.User;

public record DonorDTO(
    long id,
    User user,
    double totalDonation,
    LocalDateTime lastDonation
) {
    

   
}
