package com.bankar.taskmanager.controller;

import com.bankar.taskmanager.dto.TaskDTO;
import com.bankar.taskmanager.entity.Task;
import com.bankar.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody TaskDTO dto) {
        return taskService.createTask(dto);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
