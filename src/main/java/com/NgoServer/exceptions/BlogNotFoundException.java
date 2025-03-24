package com.NgoServer.exceptions;

public class BlogNotFoundException extends RuntimeException {

    public BlogNotFoundException(String message) {
        super(message);
    }
    
}
