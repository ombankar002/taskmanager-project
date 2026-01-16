package com.bankar.taskmanager.controller;

import com.bankar.taskmanager.dto.TaskDTO;
import com.bankar.taskmanager.entity.Task;
import com.bankar.taskmanager.service.TaskService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskViewController {

    @Autowired
    private TaskService taskService;

    private static final int PAGE_SIZE = 10;

    // Home / default
    @GetMapping("/")
    public String home(@RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Task> taskPage = taskService.getTasks(pageable);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("task", new Task());

        return "index";
    }

    // Add task
    @PostMapping("/add-task")
    public String addTask(@ModelAttribute("task") Task task,
                          RedirectAttributes redirectAttributes) {

        task.setStartTime(LocalDateTime.now()); // ⏱ auto now

        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setCompleted(false);
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());

        taskService.createTask(dto);

        redirectAttributes.addFlashAttribute("success", "Task added successfully!");
        return "redirect:/";
    }

    // Delete task
    @GetMapping("/delete-task/{id}")
    public String deleteTask(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("success", "Task deleted successfully!");
        return "redirect:/";
    }

    // Toggle completed
    @PostMapping("/toggle-task/{id}")
    public String toggleTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);

        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(!task.isCompleted());
        dto.setPriority(task.getPriority());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());

        taskService.updateTask(id, dto);
        return "redirect:/";
    }
    // Edit task page
    @GetMapping("/edit-task/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "edit-task";
    }

    // Update task
    @PostMapping("/update-task/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task task,
                             RedirectAttributes redirectAttributes) {

        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        dto.setPriority(task.getPriority());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());

        // Validation
        if (dto.getEndTime() != null && dto.getEndTime().isBefore(dto.getStartTime())) {
            redirectAttributes.addFlashAttribute(
                    "error", "End time cannot be before start time"
            );
            return "redirect:/edit-task/" + id;
        }

        taskService.updateTask(id, dto);
        redirectAttributes.addFlashAttribute("success", "Task updated successfully!");
        return "redirect:/";
    }

    // Search tasks with pagination
    @GetMapping("/search")
    public String searchTasks(@RequestParam("keyword") String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Task> taskPage = taskService.searchTasks(keyword, pageable);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("task", new Task());
        model.addAttribute("keyword", keyword);

        return "index";
    }
}
