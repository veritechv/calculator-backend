package org.challenge.calculator.exception;

/**
 * Exception thrown when a record couldn't be found in the database.
 */
public class RecordNotFoundException extends RuntimeException{
    public RecordNotFoundException(String message) {
        super(message);
    }
}
