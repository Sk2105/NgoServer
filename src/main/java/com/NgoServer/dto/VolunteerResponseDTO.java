package com.NgoServer.dto;

import java.util.List;

import com.NgoServer.models.User;
import com.NgoServer.utils.VolunteerStatus;

public record VolunteerResponseDTO(
    Long id,
    User user,
    VolunteerStatus status,
    List<EventResponseDTO> events,
    List<TaskResponseDTO> tasks

    
) {
    
}
