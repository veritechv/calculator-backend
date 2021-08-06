package org.challenge.calculator.controller;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.services.ServiceCalculatorService;
import org.challenge.calculator.model.AppService;
import org.challenge.calculator.model.AppServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@CrossOrigin
public class ServicesController {

    private ServiceCalculatorService calculatorServiceService;

    @Autowired
    public ServicesController(ServiceCalculatorService calculatorServiceService) {
        this.calculatorServiceService = calculatorServiceService;
    }

    @GetMapping("/list")
    public Page<AppService> listServices(@RequestParam(defaultValue="0")Integer pageIndex,
                                         @RequestParam(defaultValue="20")Integer pageSize,
                                         @RequestParam(defaultValue="name")String sortBy){

        return AppServiceFactory.buildFromPageService(calculatorServiceService.listServices(pageIndex,
                pageSize,sortBy));
    }

}
