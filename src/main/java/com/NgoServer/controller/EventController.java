package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.NgoServer.dto.EventBodyDTO;
import com.NgoServer.services.EventService;

@RequestMapping("/events")
@RestController
public class EventController {


    @Autowired
    private EventService eventService;


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEvent(@RequestBody EventBodyDTO eventBodyDTO) {
        return ResponseEntity.ok(eventService.createEvent(eventBodyDTO));
    }


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventBodyDTO eventBodyDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventBodyDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<?> registerVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.registerVolunteer(id));
    }
    
}
