package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.TaskBodyDTO;
import com.NgoServer.dto.TaskResponseDTO;
import com.NgoServer.models.Task;
import com.NgoServer.models.Volunteer;
import com.NgoServer.repo.TaskRepository;
import com.NgoServer.repo.VolunteerRepository;
import com.NgoServer.utils.TaskStatus;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAllTasks();
    }

    public ResponseDTO createTask(TaskBodyDTO task) {
        try {
            Task taskEntity = toTask(task);
            taskRepository.save(taskEntity);
            return new ResponseDTO("Task created successfully", HttpStatus.CREATED.value());
        } catch (Exception e) {
            throw new RuntimeException("Error creating task: " + e.getMessage());
        }
    }

    private Task toTask(TaskBodyDTO taskBodyDTO) {
        Task task = new Task();
        System.out.println(taskBodyDTO);
        task.setTitle(taskBodyDTO.title());
        task.setDescription(taskBodyDTO.description());
        task.setStartedAt(toLocalDateTime(taskBodyDTO.startAt()));
        task.setEndedAt(toLocalDateTime(taskBodyDTO.endAt()));

        return task;
    }

    private LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime);
    }

    public ResponseDTO deleteTask(Long id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseDTO("Task deleted successfully", HttpStatus.OK.value());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        }
    }

    public ResponseDTO updateTask(Long id, TaskBodyDTO taskBodyDTO) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
            task.setTitle(taskBodyDTO.title());
            task.setDescription(taskBodyDTO.description());
            task.setStartedAt(toLocalDateTime(taskBodyDTO.startAt()));
            task.setEndedAt(toLocalDateTime(taskBodyDTO.endAt()));

            taskRepository.save(task);
            return new ResponseDTO("Task updated successfully", HttpStatus.OK.value());
        } catch (Exception e) {
            throw new RuntimeException("Error updating task: " + e.getMessage());
        }
    }

    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }

    public ResponseDTO assignTaskToVolunteer(Long taskId, Long volunteerId) {
        try {
            Task task = taskRepository.findTaskById(taskId);
            if (task == null) {
                throw new RuntimeException("Task not found");
            }
            Volunteer volunteer = volunteerRepository.findAllVolunteers().stream()
                    .filter(v -> v.getId().equals(volunteerId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            if (volunteer == null) {
                throw new RuntimeException("Volunteer not found");
            }
            if (task.getVolunteers().stream().anyMatch(v -> v.getId().equals(volunteerId))) {
                throw new RuntimeException("Task already assigned to this volunteer");
            }
            volunteer.getTasks().add(task);
            volunteerRepository.save(volunteer);
            return new ResponseDTO("Task assigned to volunteer successfully", HttpStatus.OK.value());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseDTO unassignTaskFromVolunteer(Long taskId, Long volunteerId) {
        try {
            Task task = taskRepository.findTaskById(taskId);
            if (task == null) {
                throw new RuntimeException("Task not found");
            }
            Volunteer volunteer = volunteerRepository.findAllVolunteers().stream()
                    .filter(v -> v.getId().equals(volunteerId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            if (volunteer == null) {
                throw new RuntimeException("Volunteer not found");
            }
            List<Volunteer> volunteers = task.getVolunteers().stream().filter(
                    v -> !v.getId().equals(volunteerId)).toList();
            task.setVolunteers(volunteers);
            List<Task> tasks = volunteer.getTasks().stream().filter(t -> !t.getId().equals(taskId)).toList();
            volunteer.setTasks(tasks);
            volunteerRepository.save(volunteer);
            return new ResponseDTO("Task unassigned from volunteer successfully", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseDTO updateTaskStatus(Long taskId, TaskStatus status) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
            task.setStatus(status);
            taskRepository.save(task);

            return new ResponseDTO("Task Update Successfully", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Task Not Updated");
        }
    }

}
