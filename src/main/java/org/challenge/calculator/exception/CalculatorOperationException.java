package org.challenge.calculator.exception;

/**
 * This exceptions is used when the service we want to execute doesn't
 * meet the necessary requirements, eg: is inactive, or the provided parameters are not correct.
 */
public class CalculatorOperationException extends RuntimeException{
    public CalculatorOperationException(String message) {
        super(message);
    }
}
