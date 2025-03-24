package com.NgoServer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.BlogAlreadyExistsException;
import com.NgoServer.exceptions.BlogNotFoundException;
import com.NgoServer.exceptions.UserNotFoundException;

@RestControllerAdvice
public class BlogExceptionHandler {

     @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<?> handleBlogNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }
    @ExceptionHandler(BlogAlreadyExistsException.class)
    public ResponseEntity<?> handleBlogAlreadyExistsException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    
}
