package com.NgoServer.exceptions;


public class BlogAlreadyExistsException extends RuntimeException {

    public BlogAlreadyExistsException(String message) {
        super(message);
    }
    
}
