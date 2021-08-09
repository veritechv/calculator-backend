package org.challenge.calculator.exception;

/**
 * Exception thrown when there is no enough balance to execute a service.
 */
public class InsufficientBalanceForExecution extends RuntimeException{
    public InsufficientBalanceForExecution(String message) {
        super(message);
    }
}
