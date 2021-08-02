package org.challenge.calculator.services;

import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;

public interface CalculatorService {
    ServiceResponse execute(ServiceRequest serviceRequest);
}
