package com.NgoServer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.FoundEmptyElementException;
import com.NgoServer.exceptions.UserAlreadyExists;
import com.NgoServer.exceptions.UserNotFoundException;

@RestControllerAdvice
public class AuthExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExists e) {
        return ResponseEntity.status(409).body(new ResponseDTO(e.getMessage(), 409));
    }

    @ExceptionHandler(FoundEmptyElementException.class)
    public ResponseEntity<?> handleFoundEmptyElementException(FoundEmptyElementException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
