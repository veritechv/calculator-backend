package org.challenge.calculator.exception;

/**
 * This is the main exception we are going to handle through the application.
 * The idea is to have an homogeneous way to handle the exceptions.
 * Each exception holds the cause so we can identify and act accordingly.
 *
 */
public class CalculatorException extends RuntimeException{
    private ErrorCause cause;

    /**
     * @param message Error description.
     * @param cause Why this happened.
     */
    public CalculatorException(String message, ErrorCause cause) {
        super(message);
        this. cause = cause;
    }
}
