package org.challenge.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.model.AppService;
import org.challenge.calculator.model.AppServiceFactory;
import org.challenge.calculator.services.ServiceCalculatorService;
import org.challenge.calculator.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "calculatorapi")
@RestController
@RequestMapping("/api/v1/services")
@CrossOrigin
public class ServicesController {
    private ServiceCalculatorService calculatorServiceService;

    @Autowired
    public ServicesController(ServiceCalculatorService calculatorServiceService) {
        this.calculatorServiceService = calculatorServiceService;
    }

    @Operation(summary = "Gets the list of all the registered services in the app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the services found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "If the pagination parameters are wrong",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping("/list/admin")
    public Page<AppService> listServicesForAdmin(@Parameter(description = "Zero based index that indicates the page we want to retrieve")
                                                 @RequestParam(defaultValue = "0") Integer pageIndex,
                                                 @Parameter(description = "Number of records per page. Default is 20.")
                                                 @RequestParam(defaultValue = "20") Integer pageSize,
                                                 @Parameter(description = "Field name used to sort the returned elements. Default is name")
                                                 @RequestParam(defaultValue = "name") String sortBy) {

        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServicesForAdmin(pageIndex,
                pageSize, sortBy));
    }

    @Operation(summary = "Gets the list of all the services available for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the services found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "If the pagination parameters are wrong",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping("/list")
    public Page<AppService> listServices(@Parameter(description = "Zero based index that indicates the page we want to retrieve")
                                         @RequestParam(defaultValue = "0") Integer pageIndex,
                                         @Parameter(description = "Number of records per page. Default is 20.")
                                         @RequestParam(defaultValue = "20") Integer pageSize,
                                         @Parameter(description = "Field name used to sort the returned elements. Default is name")
                                         @RequestParam(defaultValue = "name") String sortBy) {

        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServices(pageIndex,
                pageSize, sortBy));
    }


    @Operation(summary = "Gets the list of all the different service types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the services found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})})
    @GetMapping("/types")
    public ResponseEntity<List<String>> serviceTypes() {
        return new ResponseEntity<>(calculatorServiceService.getServiceTypes(), HttpStatus.OK);
    }

    @Operation(summary = "Updates a service's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the service with the new data.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppService.class))}),
            @ApiResponse(responseCode = "404", description = "If service we want to update doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "If the new data is not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @PutMapping()
    public ResponseEntity<AppService> updateService(@RequestBody AppService service) {
        Service updatedService = calculatorServiceService.updateService(AppServiceFactory.buildFromAppService(service));
        return new ResponseEntity<>(AppServiceFactory.buildFromService(updatedService), HttpStatus.OK);
    }

    @Operation(summary = "Deletes an specific service identified by it's uuid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returs an ACK message for the deletion",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "If the service we want to delete doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteService(@PathVariable(name = "uuid") String serviceUuid) {
        calculatorServiceService.deleteService(serviceUuid);
        return new ResponseEntity(JsonUtil.buildJsonSimpleResponse("Service deleted successfully!."), HttpStatus.OK);
    }


    @Operation(summary = "Registers a new service with the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returs the registered service",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppService.class))}),
            @ApiResponse(responseCode = "404", description = "If the service data is incomplete or not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @PostMapping
    public ResponseEntity<AppService> registerService(@RequestBody AppService newService) {
        ResponseEntity<AppService> response = null;
        if (newService != null) {
            newService = AppServiceFactory.buildFromService(
                    calculatorServiceService.createService(
                            AppServiceFactory.buildFromAppService(newService)));
            response = new ResponseEntity<>(newService, HttpStatus.OK);
        }
        return response;
    }

}
