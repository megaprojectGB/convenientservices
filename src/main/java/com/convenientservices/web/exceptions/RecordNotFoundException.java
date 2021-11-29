package com.convenientservices.web.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordNotFoundException extends Exception{

    private final Logger logger = LoggerFactory.getLogger(RecordNotFoundException.class);

    public RecordNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
