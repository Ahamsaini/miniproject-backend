package com.mainApp.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException {
    private HttpStatus status;
    private String code;

    public AuthenticationException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
        this.code = "AUTHENTICATION_FAILED";
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.UNAUTHORIZED;
        this.code = "AUTHENTICATION_FAILED";
    }

    // If you want to have different status and code, you can add a constructor with parameters

    public AuthenticationException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public AuthenticationException(String message, Throwable cause, HttpStatus status, String code) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    // Getters for status and code
    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    // Inner exceptions
    public static class InvalidCredentialsException extends AuthenticationException {
        public InvalidCredentialsException() {
            super("Invalid username or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
        }
    }

    public static class AccountDisabledException extends AuthenticationException {
        public AccountDisabledException() {
            super("Account is disabled", HttpStatus.UNAUTHORIZED, "ACCOUNT_DISABLED");
        }
    }

    // ... and so on for the other inner exceptions
}