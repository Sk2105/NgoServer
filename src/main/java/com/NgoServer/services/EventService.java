package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.EventBodyDTO;
import com.NgoServer.dto.EventResponseDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.models.Event;
import com.NgoServer.models.User;
import com.NgoServer.models.Volunteer;
import com.NgoServer.repo.EventRepository;
import com.NgoServer.repo.VolunteerRepository;
import com.NgoServer.utils.VolunteerStatus;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthServices authServices;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public ResponseDTO createEvent(EventBodyDTO eventBodyDTO) {

        Event event = new Event();
        event.setTitle(eventBodyDTO.title());
        event.setDescription(eventBodyDTO.description());
        event.setImage(eventBodyDTO.image());
        event.setLocation(eventBodyDTO.location());
        event.setStartDate(eventBodyDTO.startDate());
        event.setEndDate(eventBodyDTO.endDate());
        event.setStatus(eventBodyDTO.status());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(event);
        return new ResponseDTO("Event created successfully", 200);
    }

    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAllEvents();
    }

    public Event getEventById(Long id) {
        Event event = eventRepository.getEventById(id);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }
        return event;
    }

    public ResponseDTO deleteEvent(Long id) {
        eventRepository.deleteById(id);
        return new ResponseDTO("Event deleted successfully", 200);
    }

    public ResponseDTO updateEvent(Long id, EventBodyDTO eventBodyDTO) {
        Event event = eventRepository.getEventById(id);
        if (event == null) {
            throw new RuntimeException("Event not found");
        }
        event.setTitle(eventBodyDTO.title());
        event.setDescription(eventBodyDTO.description());
        event.setImage(eventBodyDTO.image());
        event.setLocation(eventBodyDTO.location());
        event.setStartDate(eventBodyDTO.startDate());
        event.setEndDate(eventBodyDTO.endDate());
        event.setStatus(eventBodyDTO.status());
        event.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(event);
        return new ResponseDTO("Event updated successfully", 200);
    }

    public ResponseDTO registerVolunteer(Long eventId) {
        try {
            Event event = getEventById(eventId);
            if (event == null) {
                throw new RuntimeException("Event not found with ID: " + eventId);
            }

            User currentUser = authServices.getCurrentUserDetails();
            Volunteer volunteer = eventRepository.findVolunteerByUserId(currentUser.getId());

            if (volunteer == null) {
                throw new RuntimeException("Volunteer not found for user ID: " + currentUser.getId());
            }

            if (volunteer.getStatus() == VolunteerStatus.PENDING) {
                throw new RuntimeException("Volunteer is not allowed to register for this event");
            }

            if (volunteer.getStatus() == VolunteerStatus.BANNED) {
                throw new RuntimeException("Volunteer is banned from registering for events");
            }

            if (volunteer.getEvents().contains(event)) {
                throw new RuntimeException("Volunteer is already registered for this event");
            }

            volunteer.getEvents().add(event);

            volunteerRepository.save(volunteer);

            return new ResponseDTO("Volunteer registered successfully", 200);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getCurrentUserDetails() {
        return authServices.getCurrentUserDetails();
    }

}
