package com.example.kendraconnector.exceptions;

public class IndexNotFoundException extends RuntimeException {

    public IndexNotFoundException(String message) {
        super(message);
    }

    public IndexNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexNotFoundException(Throwable cause) {
        super(cause);
    }

}
