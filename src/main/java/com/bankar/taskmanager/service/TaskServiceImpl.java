package com.bankar.taskmanager.service;

import com.bankar.taskmanager.dto.TaskDTO;
import com.bankar.taskmanager.entity.Task;
import com.bankar.taskmanager.exception.TaskNotFoundException;
import com.bankar.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // ================= CREATE TASK =================
    @Override
    public Task createTask(TaskDTO dto) {
        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setPriority(dto.getPriority());

        // 🔥 TIME FIELDS (CRITICAL FOR COUNTDOWN)
        task.setStartTime(LocalDateTime.now());   // auto current time
        task.setEndTime(dto.getEndTime());        // from form

        return taskRepository.save(task);
    }

    // ================= GET ALL TASKS =================
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .sorted((t1, t2) -> {
                    if (t1.getPriority() == null && t2.getPriority() == null) return 0;
                    if (t1.getPriority() == null) return 1;
                    if (t2.getPriority() == null) return -1;
                    return t2.getPriority().ordinal() - t1.getPriority().ordinal();
                })
                .toList();
    }

    // ================= GET BY ID =================
    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));
    }

    // ================= UPDATE TASK =================
    @Override
    public Task updateTask(Long id, TaskDTO dto) {
        Task task = getTaskById(id);

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setPriority(dto.getPriority());

        // keep existing endTime unless updated
        if (dto.getEndTime() != null) {
            task.setEndTime(dto.getEndTime());
        }

        return taskRepository.save(task);
    }

    // ================= DELETE TASK =================
    @Override
    public void deleteTask(Long id) {
        taskRepository.delete(getTaskById(id));
    }

    // ================= SEARCH (NO PAGINATION) =================
    @Override
    public List<Task> searchTasks(String keyword) {
        return taskRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword
                );
    }

    // ================= PAGINATION =================
    @Override
    public Page<Task> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> searchTasks(String keyword, Pageable pageable) {
        return taskRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword, pageable
                );
    }
}
