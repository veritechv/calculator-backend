package org.challenge.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.challenge.calculator.services.CalculatorService;
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
@SecurityRequirement(name = "calculatorapi")
@RestController
@RequestMapping("/api/v1/calculator")
@CrossOrigin
public class CalculatorController {
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
    @Operation(summary = "Executes a service with the given parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The service was executed successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service receives a wrong number of parameters or the parameters are not numbers",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/execute")
    public ResponseEntity<ServiceResponse> executeService(@RequestBody ServiceRequest serviceRequest) {
        return  callService(serviceExecutor, serviceRequest);
    }


    /*************************************************************/
    /***** These endpoints were part of the first iteration ******/
    /***** before they were replaced by  /execute           ******/
    /*************************************************************/
    @Operation(summary = "Service that returns a random string. No parameters needed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) })})
    @PatchMapping("/randomString")
    public ResponseEntity<ServiceResponse> generateRandomString(@RequestBody ServiceRequest serviceRequest) {
        return callService(randomStringService, serviceRequest);
    }


    @Operation(summary = "Service that adds up two numbers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service receives a wrong number of parameters or the parameters are not numbers",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/add")
    public ResponseEntity<ServiceResponse> addNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(additionService, serviceRequest);
    }

    @Operation(summary = "Service that subtract two numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service receives a wrong number of parameters or the parameters are not numbers",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/sub")
    public ResponseEntity<ServiceResponse> subtractNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(subtractionService, serviceRequest);
    }

    @Operation(summary = "Service multiplies two numbers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service receives a wrong number of parameters or the parameters are not numbers",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/multiply")
    public ResponseEntity<ServiceResponse> multiplyNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(multiplicationService, serviceRequest);
    }

    @Operation(summary = "Service that divides two numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service receives a wrong number of parameters or the parameters are not numbers" +
                            "or the second parameter is zero",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/divide")
    public ResponseEntity<ServiceResponse> divideNumbers(@RequestBody ServiceRequest serviceRequest) {
        return callService(divisionService, serviceRequest);
    }

    @Operation(summary = "Service that gets the square root of a number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service didn't receive at least one parameter, or the paremeter is not a number" +
                            "or the parameter is a negative number",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/squareroot")
    public ResponseEntity<ServiceResponse> squareRootOfNumber(@RequestBody ServiceRequest serviceRequest) {
        return callService(squareRootService, serviceRequest);
    }

    @Operation(summary = "Service that evaluates a mathematical expression like: 1 + 2 * (3 / 4)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When the service was executed successfully ",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "When the service didn't receive a parameter, or any of the operands are not numbers.",
                    content = { @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)) })})
    @PatchMapping("/freeform")
    public ResponseEntity<ServiceResponse> calculateFreeFormOperation(@RequestBody ServiceRequest serviceRequest) {
        return callService(freeFormService, serviceRequest);
    }

    /*
     * Method to handle the exceptions that may happen
     */
    private ResponseEntity<ServiceResponse> callService(CalculatorService service, ServiceRequest serviceRequest) {
        ResponseEntity<ServiceResponse> responseEntity;
        ServiceResponse serviceResponse = service.execute(serviceRequest);
        responseEntity = new ResponseEntity(serviceResponse, HttpStatus.OK);
        return responseEntity;
    }

}
