package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.VolunteerStatusDTO;
import com.NgoServer.services.VolunteerService;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {


    @Autowired
    private VolunteerService volunteerService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getVolunteerById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.getVolunteerById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateVolunteerStatus(@PathVariable Long id, @RequestBody VolunteerStatusDTO status) {
        return ResponseEntity.ok().body(volunteerService.updateVolunteerStatus(id, status.status()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok().body(volunteerService.deleteVolunteer(id));
    }
    
}
