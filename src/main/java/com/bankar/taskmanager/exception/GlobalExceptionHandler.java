package com.bankar.taskmanager.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // UI error page
 //   @ExceptionHandler(TaskNotFoundException.class)
  //  public String handleTaskNotFound(TaskNotFoundException ex, Model model) {
   //     model.addAttribute("errorMessage", ex.getMessage());
    //    return "error";
   // }

    // API error response
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
