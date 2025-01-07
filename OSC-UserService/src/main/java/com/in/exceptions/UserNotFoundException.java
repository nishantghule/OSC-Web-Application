package com.in.exceptions;

public class UserNotFoundException extends RuntimeException{
    private int statusCode;
    public UserNotFoundException(int statusCode) {
        super("Error occurred with status: " + statusCode);
        this.statusCode = statusCode;
    }
    // Getter for the status code
    public int getStatusCode() {
        return statusCode;
    }
}
