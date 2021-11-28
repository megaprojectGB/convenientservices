package com.convenientservices.web.Exceptions;

public class UserNotFoundException extends RecordNotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
