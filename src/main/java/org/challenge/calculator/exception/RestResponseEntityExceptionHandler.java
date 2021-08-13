package org.challenge.calculator.exception;

import org.challenge.calculator.utils.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
     * @param exception Object with the error information
     * @param request Request made to the rest controller
     * @return A ResponseEntity with a proper error message
     */
    @ExceptionHandler(value = {CalculatorException.class, RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        String bodyOfResponse = "Something wrong happened. Please contact your administrator.";
        if(exception instanceof CalculatorException){
            bodyOfResponse = exception.getMessage();
        }

        bodyOfResponse = JsonUtil.buildJsonSimpleResponse(bodyOfResponse);
        return handleExceptionInternal(exception, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
