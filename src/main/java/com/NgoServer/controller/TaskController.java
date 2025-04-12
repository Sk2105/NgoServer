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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.TaskBodyDTO;
import com.NgoServer.services.TaskService;
import com.NgoServer.utils.TaskStatus;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    /**
     * This API is used to get all tasks
     * 
     * @return List of tasks
     */
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTEER')")
    /**
     * This API is used to get task by id
     * 
     * @param id of the task
     * @return Task object
     */
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    /**
     * This API is used to create a task
     * 
     * @param taskBodyDTO contains the information of the task
     * @return ResponseDTO which contains message and status code
     */
    public ResponseEntity<?> createTask(@RequestBody TaskBodyDTO taskBodyDTO) {
        return ResponseEntity.ok(taskService.createTask(taskBodyDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * This API is used to update a task
     * 
     * @param id          of the task
     * @param taskBodyDTO contains the information of the task
     * @return ResponseDTO which contains message and status code
     */
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskBodyDTO taskBodyDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskBodyDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    /**
     * This API is used to delete a task
     * 
     * @param id of the task
     * @return ResponseDTO which contains message and status code
     */
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTask(
            @PathVariable Long id,
            @RequestParam Long volunteerId) {
        return ResponseEntity.ok().body(taskService.assignTaskToVolunteer(id, volunteerId));
    }

    @PostMapping("/{id}/unassign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unassignTask(
            @PathVariable Long id,
            @RequestParam() Long volunteerId) {
        return ResponseEntity.ok().body(taskService.unassignTaskFromVolunteer(id, volunteerId));
    }

    @PutMapping("{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    /**
     * This API is used to update the status of a task
     * 
     * @param id     of the task
     * @param status of the task
     * @return ResponseDTO which contains message and status code
     */
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam(defaultValue = "PENDING") TaskStatus status) {
        return ResponseEntity.ok().body(taskService.updateTaskStatus(id, status));
    }

}
