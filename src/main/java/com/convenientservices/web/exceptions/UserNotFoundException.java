package com.convenientservices.web.exceptions;

public class UserNotFoundException extends RecordNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
