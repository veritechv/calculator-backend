package org.challenge.calculator.controller;

import org.challenge.calculator.services.ServiceCalculatorService;
import org.challenge.calculator.model.AppService;
import org.challenge.calculator.model.AppServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicesController {

    private ServiceCalculatorService calculatorServiceService;

    @Autowired
    public ServicesController(ServiceCalculatorService calculatorServiceService) {
        this.calculatorServiceService = calculatorServiceService;
    }

    @GetMapping("/services/list")
    public Page<AppService> listServices(Pageable pageable){
        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServices(pageable));
    }

}
