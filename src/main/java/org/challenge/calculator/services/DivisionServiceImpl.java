package org.challenge.calculator.services;

import org.apache.commons.collections.CollectionUtils;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the division service.
 * It can divide integer and decimal numbers.
 */
@Service("divisionService")
public class DivisionServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DivisionServiceImpl.class);

    /**
     * @throws CalculatorException when:
     *                             - the number of parameters is not at least two
     *                             - the parameters are not numbers
     *                             - the second parameter is equal to zero
     */
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) || serviceRequest.getParameters().size() < 2) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorException("There are missing some numbers for division", ErrorCause.WRONG_NUMBER_OF_PARAMETERS);
            }

            try {
                float number1 = Float.valueOf(serviceRequest.getParameters().get(0));
                float number2 = Float.valueOf(serviceRequest.getParameters().get(1));

                if (number2 == 0) {
                    LOGGER.error("Second argument is zero. We can't divide by zero");
                    throw new CalculatorException("Second argument is zero. We can't divide by zero", ErrorCause.INVALID_PARAMETERS);
                }

                float result = number1 / number2;

                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Division parameters are not numbers");
                throw new CalculatorException("Division parameters are not numbers", ErrorCause.INVALID_PARAMETERS);
            }
        }

        return serviceResponse;
    }
}

