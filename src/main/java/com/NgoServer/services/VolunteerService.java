package com.NgoServer.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.VolunteerResponseDTO;
import com.NgoServer.models.Volunteer;
import com.NgoServer.repo.VolunteerRepository;
import com.NgoServer.utils.VolunteerStatus;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAllVolunteers();
    }

    public VolunteerResponseDTO getVolunteerById(Long id) {
        Optional<VolunteerResponseDTO> optionalVolunteer = volunteerRepository.findVolunteerById(id);
        if (optionalVolunteer.isEmpty()) {
            throw new RuntimeException("Volunteer not found with ID: " + id);
        }
        return optionalVolunteer.get();
    }

    public ResponseDTO updateVolunteerStatus(Long id, VolunteerStatus status) {
        Volunteer volunteer = volunteerRepository.findById(id).orElse(null);
        if (volunteer != null) {
            volunteer.setStatus(status);
            volunteerRepository.save(volunteer);
            return new ResponseDTO("Volunteer status updated", HttpStatus.OK.value());
        } else {
            throw new RuntimeException("Volunteer not found with ID: " + id);
        }
    }

    public ResponseDTO deleteVolunteer(Long id) {
        Volunteer volunteer = volunteerRepository.findById(id).orElse(null);
        if (volunteer != null) {
            volunteerRepository.delete(volunteer);
            return new ResponseDTO("Volunteer deleted", HttpStatus.OK.value());
        } else {
            throw new RuntimeException("Volunteer not found with ID: " + id);
        }
    }

}
