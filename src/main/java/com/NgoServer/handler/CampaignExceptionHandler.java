package com.NgoServer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.CampaignAlreadyExits;
import com.NgoServer.exceptions.CampaignNotFoundException;

@RestControllerAdvice
public class CampaignExceptionHandler {

     @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(CampaignAlreadyExits.class)
    public ResponseEntity<?> handleCampaignAlreadyExists(CampaignAlreadyExits e) {
        return ResponseEntity.status(409).body(new ResponseDTO(e.getMessage(), 409));
    }


    @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<?> handleCampaignNotFoundException(CampaignNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
    
}
