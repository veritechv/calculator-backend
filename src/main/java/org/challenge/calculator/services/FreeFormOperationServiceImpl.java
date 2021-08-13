package org.challenge.calculator.services;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * This service can solve mathematical expressions like: 1+(2*3)/4
 */
@Service("freeFormService")
public class FreeFormOperationServiceImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeFormOperationServiceImpl.class);

    /**
     * @throws CalculatorException when:
     *                             - We didn't receive a parameter
     *                             - the mathematical expression is malformed
     *                             - the parameters are not numbers
     */
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;

        if (isRequestValid(serviceRequest)) {
            if (CollectionUtils.isEmpty(serviceRequest.getParameters()) ||
                    (serviceRequest.getParameters().size() < 1) ||
                    StringUtils.isBlank(serviceRequest.getParameters().get(0))) {
                LOGGER.error("The number of arguments is wrong.");
                throw new CalculatorException("There is no math operation to evaluate", ErrorCause.WRONG_NUMBER_OF_PARAMETERS);
            }

            try {
                String expression = serviceRequest.getParameters().get(0);
                DoubleEvaluator eval = new DoubleEvaluator();
                Double result = eval.evaluate(expression);

                serviceResponse = createBasicResponseOutOfRequest(serviceRequest);
                serviceResponse.setResponse(result + "");

            } catch (NumberFormatException exception) {
                LOGGER.error("Math expression holds not-numbers");
                throw new CalculatorException("Math expression holds not-numbers", ErrorCause.INVALID_PARAMETERS);
            } catch (IllegalArgumentException exception) {
                LOGGER.error("The math expression is malformed");
                throw new CalculatorException("The math expression is malformed", ErrorCause.INVALID_PARAMETERS);
            }
        }

        return serviceResponse;

    }
}
