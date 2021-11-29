package com.convenientservices.web.exceptions;

public class CityNotFoundException extends RecordNotFoundException{
    public CityNotFoundException(String message) {
        super(message);
    }
}
