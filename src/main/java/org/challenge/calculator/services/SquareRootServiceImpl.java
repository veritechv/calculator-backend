package org.challenge.calculator.services;

import org.apache.commons.collections.CollectionUtils;
import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("squareRootService")
public class SquareRootServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SquareRootServiceImpl.class);

    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) || serviceRequest.getParameters().size() < 1) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorOperationException("We need at least one number to calculate square root");
            }

            try {
                float number1 = Float.valueOf(serviceRequest.getParameters().get(0));

                if(number1 < 0){
                    throw new CalculatorOperationException("We can't calculate the square root of negative number");
                }

                double result = Math.sqrt(number1);

                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Square root parameter is not a number");
                throw new CalculatorOperationException("Square root parameter is not a number");
            }
        }
        return serviceResponse;

    }
}
