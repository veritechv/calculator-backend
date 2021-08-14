package org.challenge.calculator.controller.exception;

import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.utils.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class concentrates the exception handling for the rest endpoints.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    /**
     * We define that we handle either a CalculatorException or any RuntimeException
     *
     * @param exception Object with the error information
     * @param request   Request made to the rest controller
     * @return A ResponseEntity with a proper error message
     */
    @ExceptionHandler(value = {CalculatorException.class, RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        String bodyOfResponse = "Something wrong happened. Please contact your administrator.";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if (exception instanceof CalculatorException) {
            bodyOfResponse = exception.getMessage();
            CalculatorException calculatorException = (CalculatorException) exception;
            switch (calculatorException.getErrorCause()) {
                case INVALID_PARAMETERS:
                case WRONG_NUMBER_OF_PARAMETERS:
                    httpStatus = HttpStatus.BAD_REQUEST;//400
                    break;
                case USER_NOT_FOUND:
                case RECORD_NOT_FOUND:
                case SERVICE_NOT_FOUND:
                    httpStatus = HttpStatus.NOT_FOUND; //404
                    break;
                case WRONG_USER_CREDENTIALS:
                    httpStatus = HttpStatus.UNAUTHORIZED;//401
                    break;
                case SERVICE_TYPE_UNSUPPORTED:
                    httpStatus = HttpStatus.NOT_IMPLEMENTED;//501
                    break;
                case USER_ALREADY_EXISTS:
                case INSUFFICIENT_BALANCE:
                case USER_NOT_ACTIVE:
                case SERVICE_NOT_ACTIVE:
                    httpStatus = HttpStatus.CONFLICT;//409
                    break;
            }
        }

        bodyOfResponse = JsonUtil.buildJsonSimpleResponse(bodyOfResponse);
        return handleExceptionInternal(exception, bodyOfResponse,
                new HttpHeaders(), httpStatus, request);
    }
}
