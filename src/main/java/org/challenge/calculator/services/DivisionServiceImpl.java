package org.challenge.calculator.services;

import org.apache.commons.collections.CollectionUtils;
import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("divisionService")
public class DivisionServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DivisionServiceImpl.class);

    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) || serviceRequest.getParameters().size() < 2) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorOperationException("There are missing some numbers for division");
            }

            try {
                float number1 = Float.valueOf(serviceRequest.getParameters().get(0));
                float number2 = Float.valueOf(serviceRequest.getParameters().get(1));

                if (number2 == 0) {
                    LOGGER.error("Second argument is zero. We can't divide by zero");
                    throw new IllegalArgumentException("Second argument is zero. We can't divide by zero");
                }

                float result = number1 / number2;

                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Division parameters are not numbers");
                throw new CalculatorOperationException("Division parameters are not numbers");
            }
        }

        return serviceResponse;
    }
}

