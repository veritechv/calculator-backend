package org.challenge.calculator.controller;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.exception.InsufficientBalanceForExecution;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.challenge.calculator.services.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorController.class);
    private CalculatorService randomStringService;

    @Autowired
    public CalculatorController(CalculatorService randomStringService) {
        this.randomStringService = randomStringService;
    }

    @GetMapping("/randomString")
    public ResponseEntity<ServiceResponse> generateRandomString(@RequestBody ServiceRequest serviceRequest){
        ResponseEntity<ServiceResponse> responseEntity;
        try {
            ServiceResponse randomStringResponse = randomStringService.execute(serviceRequest);
            responseEntity = new ResponseEntity(randomStringResponse, HttpStatus.OK);
        }catch(InsufficientBalanceForExecution exception){
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }catch(IllegalArgumentException exception){
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}
