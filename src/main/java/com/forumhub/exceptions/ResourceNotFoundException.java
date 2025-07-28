package com.forumhub.exceptions;

//Custom exception for course not found
public class ResourceNotFoundException extends RuntimeException {

    // Constructor with messages
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor just with cause
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    // Constructor with formatted message
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }


}
