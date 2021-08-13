package org.challenge.calculator.controller;

import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.challenge.calculator.services.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the main controller for the service execution.
 * Depending on the endpoint is the type of service we can execute.
 * Before calling the actual service we do a basic validation to check if
 * we have the basic information to:
 * - identify the service
 * - identify the user calling the service
 * - and the parameters needed for the execution
 * <p>
 * Notes: As a future work this controller can delegate to the service the task
 * of identify and call the appropiate service based on it's information.
 */
@RestController
@RequestMapping("/v1/calculator")
@CrossOrigin
public class CalculatorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorController.class);
    private CalculatorService randomStringService;
    private CalculatorService additionService;
    private CalculatorService subtractionService;
    private CalculatorService multiplicationService;
    private CalculatorService divisionService;
    private CalculatorService squareRootService;
    private CalculatorService freeFormService;
    private CalculatorService serviceExecutor;

    @Autowired
    public CalculatorController(CalculatorService randomStringService,
                                CalculatorService additionService,
                                CalculatorService subtractionService,
                                CalculatorService multiplicationService,
                                CalculatorService divisionService,
                                CalculatorService squareRootService,
                                CalculatorService freeFormService,
                                CalculatorService serviceExecutor) {
        this.randomStringService = randomStringService;
        this.additionService = additionService;
        this.subtractionService = subtractionService;
        this.multiplicationService = multiplicationService;
        this.divisionService = divisionService;
        this.squareRootService = squareRootService;
        this.freeFormService = freeFormService;
        this.serviceExecutor = serviceExecutor;
    }


    /**
     * This endpoint is meant to be the single entry point to execute services
     * The idea is to handle any kind of service and make easier the use of new services in the future.
     *
     * @param serviceRequest Object with data about the service we want to execute, parameters and requesting user
     * @return The result of the service along with other status information
     */
    @PatchMapping("/execute")
    public ResponseEntity<ServiceResponse> executeService(@RequestBody ServiceRequest serviceRequest) {
        ResponseEntity<ServiceResponse> response = callService(serviceExecutor, serviceRequest);
        return response;
    }


    /*************************************************************/
    /***** These endpoints were part of the first iteration ******/
    /***** before they were replaced by  /execute           ******/
    /*************************************************************/
    @PatchMapping("/randomString")
    public ResponseEntity<ServiceResponse> generateRandomString(@RequestBody ServiceRequest serviceRequest) {
        return callService(randomStringService, serviceRequest);
    }

    @PatchMapping("/add")
    public ResponseEntity<ServiceResponse> addNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(additionService, serviceRequest);
    }

    @PatchMapping("/sub")
    public ResponseEntity<ServiceResponse> subtractNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(subtractionService, serviceRequest);
    }

    @PatchMapping("/multiply")
    public ResponseEntity<ServiceResponse> multiplyNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(multiplicationService, serviceRequest);
    }

    @PatchMapping("/divide")
    public ResponseEntity<ServiceResponse> divideNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(divisionService, serviceRequest);
    }

    @PatchMapping("/squareroot")
    public ResponseEntity<ServiceResponse> squareRootOfNumber(@RequestBody ServiceRequest serviceRequest) {
        return callService(squareRootService, serviceRequest);
    }

    @PatchMapping("/freeform")
    public ResponseEntity<ServiceResponse> calculateFreeFormOperation(@RequestBody ServiceRequest serviceRequest) {
        return callService(freeFormService, serviceRequest);
    }

    /*
     * Method to handle the exceptions that may happen
     * TODO Use an exception handler
     */
    private ResponseEntity<ServiceResponse> callService(CalculatorService service, ServiceRequest serviceRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        try {
            ServiceResponse serviceResponse = service.execute(serviceRequest);
            responseEntity = new ResponseEntity(serviceResponse, HttpStatus.OK);
        } catch (CalculatorException exception) {
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
