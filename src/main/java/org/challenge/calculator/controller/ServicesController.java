package org.challenge.calculator.controller;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.exception.ServiceNotFoundException;
import org.challenge.calculator.model.AppService;
import org.challenge.calculator.model.AppServiceFactory;
import org.challenge.calculator.services.ServiceCalculatorService;
import org.challenge.calculator.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@CrossOrigin
public class ServicesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicesController.class);
    private ServiceCalculatorService calculatorServiceService;

    @Autowired
    public ServicesController(ServiceCalculatorService calculatorServiceService) {
        this.calculatorServiceService = calculatorServiceService;
    }

    @GetMapping("/list/admin")
    public Page<AppService> listServicesForAdmin(@RequestParam(defaultValue = "0") Integer pageIndex,
                                         @RequestParam(defaultValue = "20") Integer pageSize,
                                         @RequestParam(defaultValue = "name") String sortBy) {

        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServicesForAdmin(pageIndex,
                pageSize, sortBy));
    }

    @GetMapping("/list")
    public Page<AppService> listServices(@RequestParam(defaultValue = "0") Integer pageIndex,
                                         @RequestParam(defaultValue = "20") Integer pageSize,
                                         @RequestParam(defaultValue = "name") String sortBy) {

        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServices(pageIndex,
                pageSize, sortBy));
    }


    @GetMapping("/types")
    public ResponseEntity<List<String>> serviceTypes(){
        return new ResponseEntity<>(calculatorServiceService.getServiceTypes(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<AppService> updateService(@RequestBody AppService service) {
        ResponseEntity<AppService> response;
        try {

            Service updatedService = calculatorServiceService.updateService(AppServiceFactory.buildFromAppService(service));
            if (updatedService != null) {
                response = new ResponseEntity<>(AppServiceFactory.buildFromService(updatedService), HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceNotFoundException | IllegalArgumentException exception) {
            LOGGER.error(exception.getMessage());
            response = new ResponseEntity("The received information is wrong. Please verify.", HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteService(@PathVariable(name = "uuid") String serviceUuid) {
        ResponseEntity<String> response;
        try {
            calculatorServiceService.deleteService(serviceUuid);
            response = new ResponseEntity(JsonUtil.buildJsonSimpleResponse("Service deleted successfully!."), HttpStatus.OK);

        } catch (ServiceNotFoundException | IllegalArgumentException exception) {
            LOGGER.error(exception.getMessage());
            response = new ResponseEntity(JsonUtil.buildJsonSimpleResponse("The received information is wrong. Please verify."),
                    HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    @PostMapping
    public ResponseEntity<AppService> registerService(@RequestBody AppService newService) {
        ResponseEntity<AppService> response = null;
        if (newService != null) {
            try {
                newService = AppServiceFactory.buildFromService(
                        calculatorServiceService.createService(
                                AppServiceFactory.buildFromAppService(newService)));

                response = new ResponseEntity<>(newService, HttpStatus.OK);
            } catch (IllegalArgumentException exception) {
                LOGGER.error("Something failed while creating new service.\n"+exception.getMessage());
                response = new ResponseEntity("The received information is wrong. Please verify.",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return response;
    }

}
