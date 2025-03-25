package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.EventResponseDTO;
import com.NgoServer.models.Event;
import com.NgoServer.models.User;
import com.NgoServer.models.Volunteer;
import com.NgoServer.utils.EventStatus;
import com.NgoServer.utils.Role;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT e.id, e.title, e.description, e.image, e.location, e.startDate, e.endDate, e.createdAt, e.updatedAt, e.status
            FROM Event e
            """)
    List<Object[]> findAllEventsObjects();

    default List<EventResponseDTO> findAllEvents() {
        return findAllEventsObjects().stream()
                .map(this::toEventResponseDTO)
                .toList();
    }

    private EventResponseDTO toEventResponseDTO(Object[] objects) {
        Long id = (Long) objects[0];
        String title = (String) objects[1];
        String description = (String) objects[2];
        String image = (String) objects[3];
        String location = (String) objects[4];
        LocalDateTime startDate = toLocalDateTime(objects[5]);
        LocalDateTime endDate = toLocalDateTime(objects[6]);
        LocalDateTime createdAt = toLocalDateTime(objects[7]);
        LocalDateTime updatedAt = toLocalDateTime(objects[8]);
        EventStatus status = (EventStatus) objects[9];
        return new EventResponseDTO(
                id,
                title,
                description,
                image,
                location,
                startDate,
                endDate,
                status,
                createdAt,
                updatedAt);
    }

    private LocalDateTime toLocalDateTime(Object obj) {
        if (obj instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        } else if (obj instanceof LocalDateTime ldt) {
            return ldt;
        }
        return null;
    }

    @Query("""
            SELECT e.id, e.title, e.description, e.image, e.location, e.startDate, e.endDate, e.createdAt, e.updatedAt, e.status
            FROM Event e
            WHERE e.id = :id
            """)
    public List<Object[]> getEventByIdObjects(Long id);

    default Event getEventById(Long id) {
        List<Volunteer> volunteers = getVolunteersByEventId(id);
        return getEventByIdObjects(id).stream()
                .map(obj -> {
                    Event event = toEvent(obj);
                    event.setVolunteers(volunteers);
                    return event;
                })
                .findFirst()
                .orElse(null);
    }

    private Event toEvent(Object[] objects) {
        Long id = (Long) objects[0];
        String title = (String) objects[1];
        String description = (String) objects[2];
        String image = (String) objects[3];
        String location = (String) objects[4];
        LocalDateTime startDate = toLocalDateTime(objects[5]);
        LocalDateTime endDate = toLocalDateTime(objects[6]);
        LocalDateTime createdAt = toLocalDateTime(objects[7]);
        LocalDateTime updatedAt = toLocalDateTime(objects[8]);
        EventStatus status = (EventStatus) objects[9];
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setDescription(description);
        event.setImage(image);
        event.setLocation(location);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setCreatedAt(createdAt);
        event.setUpdatedAt(updatedAt);
        event.setStatus(status);
        return event;
    }

    @Query("""
            SELECT v.id, u.id, u.username, u.email, u.phoneNumber, u.createdAt
            FROM Event e
            JOIN e.volunteers v
            JOIN v.user u
            WHERE e.id = :eventId
            """)
    public List<Object[]> getVolunteersByEventIdObject(Long eventId);

    default List<Volunteer> getVolunteersByEventId(Long eventId) {
        return getVolunteersByEventIdObject(eventId).stream()
                .map(this::toVolunteer)
                .toList();
    }

    private Volunteer toVolunteer(Object[] objects) {
        Long id = (Long) objects[0];
        Long userId = (Long) objects[1];
        String username = (String) objects[2];
        String email = (String) objects[3];
        String phoneNumber = (String) objects[4];
        LocalDateTime createdAt = toLocalDateTime(objects[5]);
        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(createdAt);
        user.setRole(Role.VOLUNTEER);
        volunteer.setUser(user);
        return volunteer;
    }

    @Query("""
            SELECT v.id, u.id, u.username, u.email, u.phoneNumber, u.createdAt
            FROM Volunteer v
            JOIN v.user u
            WHERE u.id = :userId
            """)
    List<Object[]> findVolunteerDetailsByUserId(Long userId);

    default Volunteer findVolunteerByUserId(Long userId) {
        return findVolunteerDetailsByUserId(userId).stream()
                .map(this::toVolunteer)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }
}
