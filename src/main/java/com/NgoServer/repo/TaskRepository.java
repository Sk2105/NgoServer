package com.NgoServer.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.TaskResponseDTO;
import com.NgoServer.models.Task;
import com.NgoServer.models.Volunteer;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT new com.NgoServer.dto.TaskResponseDTO(t.id, t.title, t.description,t.status, t.createdAt, t.startedAt, t.endedAt)
            FROM Task t ORDER BY t.id DESC
                """)
    List<TaskResponseDTO> findAllTasks();

    @Query("""
            SELECT new com.NgoServer.models.Task(t.id, t.title, t.description,t.status, t.createdAt, t.startedAt, t.endedAt)
            FROM Task t
            WHERE t.id = :id ORDER BY t.id DESC
            """)
    List<Task> findTaskByIdObject(Long id);

    default Task findTaskById(Long taskId) {
        return findTaskByIdObject(taskId)
                .stream()
                .map(task -> {
                    List<Volunteer> volunteers = findVolunteersByTaskId(task.getId());
                    task.setVolunteers(volunteers);
                    return task;
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Retrieves a list of {@link Volunteer} objects associated with a given
     * {@link Task}.
     *
     * @param taskId the ID of the task to find volunteers for
     * @return a list of {@link Volunteer} objects, or an empty list if no
     *         volunteers are found
     */
    @Query("""
            Select new com.NgoServer.models.Volunteer(v.id,
            new com.NgoServer.models.User(v.user.id, v.user.username, v.user.email, v.user.phoneNumber, v.user.createdAt, v.user.role), v.status)
            FROM Volunteer v
            JOIN v.tasks t
            WHERE t.id = :taskId ORDER BY v.id DESC
                """)
    List<Volunteer> findVolunteersByTaskId(Long taskId);
}
