package org.challenge.calculator.exception;

/**
 * Exception thrown when we try to create a user with a username that is
 * already assigned.
 */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
