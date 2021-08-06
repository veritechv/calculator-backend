package org.challenge.calculator.controller;

import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.exception.InsufficientBalanceForExecution;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.challenge.calculator.services.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calculator")
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

    @Autowired
    public CalculatorController(CalculatorService randomStringService, CalculatorService additionService,
                                CalculatorService subtractionService, CalculatorService multiplicationService,
                                CalculatorService divisionService, CalculatorService squareRootService,
                                CalculatorService freeFormService) {
        this.randomStringService = randomStringService;
        this.additionService = additionService;
        this.subtractionService = subtractionService;
        this.multiplicationService = multiplicationService;
        this.divisionService = divisionService;
        this.squareRootService = squareRootService;
        this.freeFormService = freeFormService;
    }

    @GetMapping("/randomString")
    public ResponseEntity<ServiceResponse> generateRandomString(@RequestBody ServiceRequest serviceRequest){
        return callService(randomStringService, serviceRequest);
    }

    @GetMapping("/add")
    public ResponseEntity<ServiceResponse> addNumbers(@RequestBody ServiceRequest serviceRequest){
        return callService(additionService, serviceRequest);
    }

    @GetMapping("/sub")
    public ResponseEntity<ServiceResponse> subtractNumbers(@RequestBody ServiceRequest serviceRequest){
        return callService(subtractionService, serviceRequest);
    }

    @GetMapping("/multiply")
    public ResponseEntity<ServiceResponse> multiplyNumbers(@RequestBody ServiceRequest serviceRequest){
        return callService(multiplicationService, serviceRequest);
    }

    @GetMapping("/divide")
    public ResponseEntity<ServiceResponse> divideNumbers(@RequestBody ServiceRequest serviceRequest){
        return callService(divisionService, serviceRequest);
    }

    @GetMapping("/squareroot")
    public ResponseEntity<ServiceResponse> squareRootOfNumber(@RequestBody ServiceRequest serviceRequest){
        return callService(squareRootService, serviceRequest);
    }

    @GetMapping("/freeform")
    public ResponseEntity<ServiceResponse> calculateFreeFormOperation(@RequestBody ServiceRequest serviceRequest){
        return callService(freeFormService, serviceRequest);
    }

    private ResponseEntity<ServiceResponse> callService(CalculatorService service, ServiceRequest serviceRequest){
        ResponseEntity<ServiceResponse> responseEntity;
        try {
            ServiceResponse serviceResponse = service.execute(serviceRequest);
            responseEntity = new ResponseEntity(serviceResponse, HttpStatus.OK);
        }catch(InsufficientBalanceForExecution exception){
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }catch(CalculatorOperationException exception){
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}
