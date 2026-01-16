package com.bankar.taskmanager.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bankar.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Page<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
	        String title, String description, Pageable pageable
	);
    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description
    );
}
