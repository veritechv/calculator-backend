package org.challenge.calculator.services;

import org.apache.commons.collections.CollectionUtils;
import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("multiplicationService")
public class MultiplicationServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiplicationServiceImpl.class);
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) || serviceRequest.getParameters().size() < 2) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorOperationException("There are missing some numbers for multiplication");
            }

            try {
                float number1 = Float.valueOf(serviceRequest.getParameters().get(0));
                float number2 = Float.valueOf(serviceRequest.getParameters().get(1));
                float result = number1 * number2;
                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Multiplication parameters are not numbers");
                throw new CalculatorOperationException("Multiplication parameters are not numbers");
            }
        }
        return serviceResponse;
    }
}