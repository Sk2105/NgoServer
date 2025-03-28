package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.EventResponseDTO;
import com.NgoServer.dto.VolunteerResponseDTO;
import com.NgoServer.models.User;
import com.NgoServer.models.Volunteer;
import com.NgoServer.utils.EventStatus;
import com.NgoServer.utils.Role;
import com.NgoServer.utils.VolunteerStatus;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    @Query("""
            Select new com.NgoServer.models.Volunteer(v.id,
            new com.NgoServer.models.User(v.user.id, v.user.username, v.user.email, v.user.phoneNumber, v.user.createdAt, v.user.role),
             v.status) from Volunteer v
            JOIN v.user u
                """)
    List<Volunteer> findAllVolunteers();

    /**
     * Find a volunteer by ID.
     *
     * @param volunteerId the ID of the volunteer to find
     * @return an Optional containing the VolunteerResponseDTO if found, otherwise
     *         empty
     */
    @Query("""
            SELECT v.id, u.id, u.username, u.email, u.phoneNumber, u.createdAt,
            v.status
            FROM Volunteer v
            JOIN v.user u
            WHERE v.id = :volunteerId
            """)
    List<Object[]> findVolunteerByIdObjects(Long volunteerId);

    /**
     * Find a volunteer by ID.
     *
     * @param id the ID of the volunteer to find
     * @return an Optional containing the VolunteerResponseDTO if found, otherwise empty
     */
    default Optional<VolunteerResponseDTO> findVolunteerById(Long id) {
        return findVolunteerByIdObjects(id).stream()
                .map(objects -> {
                    Long userId = (Long) objects[1];
                    String username = (String) objects[2];
                    String email = (String) objects[3];
                    String phoneNumber = (String) objects[4];
                    LocalDateTime userCreatedAt = (LocalDateTime) objects[5];

                    VolunteerStatus status = (VolunteerStatus) objects[6];

                    User user = new User(
                            userId,
                            username,
                            email,
                            phoneNumber,
                            userCreatedAt,
                            Role.VOLUNTEER);

                    List<EventResponseDTO> events = findEventsByVolunteerId(id);

                    return new VolunteerResponseDTO(id, user, status, events);
                })
                .findFirst();
    }

    /**
     * Retrieves a list of events associated with a specific volunteer.
     *
     * @param volunteerId the ID of the volunteer
     * @return a list of EventResponseDTO objects
     */
    @Query(value = """
            SELECT
                e.id, e.title, e.description, e.image, e.location,
                e.start_date, e.end_date, e.status, e.created_at, e.updated_at
            FROM volunteers_events ve
            JOIN events e ON ve.events_id = e.id
            JOIN volunteers v ON ve.volunteers_id = v.id
            WHERE v.id = :volunteerId
            """, nativeQuery = true)
    List<Object[]> findEventsByVolunteerIdObjects(Long volunteerId);

    default List<EventResponseDTO> findEventsByVolunteerId(Long volunteerId) {
        return findEventsByVolunteerIdObjects(volunteerId).stream()
                .map(objects -> {
                    Long eventId = (Long) objects[0];
                    String title = (String) objects[1];
                    String description = (String) objects[2];
                    String image = (String) objects[3];
                    String location = (String) objects[4];
                    LocalDateTime startDate = (LocalDateTime) toLocalDateTime(objects[5]);
                    LocalDateTime endDate = (LocalDateTime) toLocalDateTime(objects[6]);
                    String status = (String) objects[7];
                    LocalDateTime createdAt = (LocalDateTime) toLocalDateTime(objects[8]);
                    LocalDateTime updatedAt = (LocalDateTime) toLocalDateTime(objects[9]);
                    return new EventResponseDTO(
                            eventId,
                            title,
                            description,
                            image,
                            location,
                            startDate,
                            endDate,
                            EventStatus.valueOf(status),
                            createdAt,
                            updatedAt);
                })
                .toList();
    }

    private LocalDateTime toLocalDateTime(Object object) {
        if (object instanceof LocalDateTime) {
            return (LocalDateTime) object;
        } else if(object instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }

}
