package com.convenientservices.web.Exceptions;

public class CityNotFoundException extends RecordNotFoundException{
    public CityNotFoundException(String message) {
        super(message);
    }
}
