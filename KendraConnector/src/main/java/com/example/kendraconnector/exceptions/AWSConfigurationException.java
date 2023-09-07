package com.example.kendraconnector.exceptions;

public class AWSConfigurationException extends RuntimeException {
    public AWSConfigurationException(String message) {
        super(message);
    }

    public AWSConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}