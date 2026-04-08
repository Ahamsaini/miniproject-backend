package com.mainApp.exception;



import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends CustomException {
    
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.CONFLICT,
            "RESOURCE_ALREADY_EXISTS"
        );
    }
    
    public ResourceAlreadyExistsException(String resourceName) {
        super(
            String.format("%s already exists", resourceName),
            HttpStatus.CONFLICT,
            "RESOURCE_ALREADY_EXISTS"
        );
    }
    
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }
}
