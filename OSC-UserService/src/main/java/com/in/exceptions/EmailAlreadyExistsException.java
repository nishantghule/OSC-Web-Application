package com.in.exceptions;


public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String email;

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

