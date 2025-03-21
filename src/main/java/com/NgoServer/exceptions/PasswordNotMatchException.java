package com.NgoServer.exceptions;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException(String message){
        super(message);
    }
    
}
