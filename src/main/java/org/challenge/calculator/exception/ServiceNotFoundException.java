package org.challenge.calculator.exception;

/**
 * Exception thrown when a service couldn't be found in the database.
 */
public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
