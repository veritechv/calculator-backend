package org.challenge.calculator.controller;

import org.challenge.calculator.services.CalculatorServiceService;
import org.challenge.calculator.webmodel.AppService;
import org.challenge.calculator.webmodel.AppServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicesController {

    private CalculatorServiceService calculatorServiceService;

    @Autowired
    public ServicesController(CalculatorServiceService calculatorServiceService) {
        this.calculatorServiceService = calculatorServiceService;
    }

    @GetMapping("/services/list")
    public Page<AppService> listServices(Pageable pageable){
        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServices(pageable));
    }

}
