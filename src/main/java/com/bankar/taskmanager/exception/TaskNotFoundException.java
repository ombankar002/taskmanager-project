package com.bankar.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L; 
    public TaskNotFoundException(String message) {
        super(message);
    }
}
