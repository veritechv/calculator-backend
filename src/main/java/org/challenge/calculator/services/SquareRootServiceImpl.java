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
 * This service calculates the square root of a number
 */
@Service("squareRootService")
public class SquareRootServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SquareRootServiceImpl.class);

    /**
     * /**
     *
     * @throws CalculatorException when:
     *                             - No parameters were passed in the request.
     *                             - The parameter is a negative number
     *                             - The parameter is no not number
     */
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) || serviceRequest.getParameters().size() < 1) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorException("We need at least one number to calculate square root",
                        ErrorCause.WRONG_NUMBER_OF_PARAMETERS);
            }

            try {
                float number1 = Float.valueOf(serviceRequest.getParameters().get(0));

                if (number1 < 0) {
                    throw new CalculatorException("We can't calculate the square root of negative number",
                            ErrorCause.INVALID_PARAMETERS);
                }

                double result = Math.sqrt(number1);

                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Square root parameter is not a number");
                throw new CalculatorException("Square root parameter is not a number", ErrorCause.INVALID_PARAMETERS);
            }
        }
        return serviceResponse;

    }
}
