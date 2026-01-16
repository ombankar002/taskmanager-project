package com.bankar.taskmanager.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bankar.taskmanager.dto.TaskDTO;
import com.bankar.taskmanager.entity.Task;
import java.util.List;

public interface TaskService {
    Task createTask(TaskDTO dto);
    List<Task> searchTasks(String keyword);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task updateTask(Long id, TaskDTO dto);
    void deleteTask(Long id);
    Page<Task> getTasks(Pageable pageable);
    Page<Task> searchTasks(String keyword, Pageable pageable);
}
