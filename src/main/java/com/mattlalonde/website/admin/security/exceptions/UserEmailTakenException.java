package com.mattlalonde.website.admin.security.exceptions;

public class UserEmailTakenException extends RuntimeException {

    public UserEmailTakenException(String message) { super(message); }

    public UserEmailTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
