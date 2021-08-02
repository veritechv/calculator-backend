package org.challenge.calculator.exception;

public class InsufficientBalanceForExecution extends RuntimeException{
    public InsufficientBalanceForExecution(String message) {
        super(message);
    }
}
