package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * The purpose of this class is to serve as a template for future services.
 * It defines common functionality for all services.
 */
public abstract class CalculatorService {

    /**
     * Main method to perform a calculator operation.
     *
     * @param serviceRequest Object holding the data needed by the service to execute.
     * @return the result of the execution.
     */
    public abstract ServiceResponse execute(ServiceRequest serviceRequest);

    /**
     * Method that checks if the request received holds the basic information that a service might need.
     *
     * @param serviceRequest Object holding the data needed by the service to execute.
     * @return True if the uuid and the username are in the request, false otherwise.
     */
    public boolean isRequestValid(ServiceRequest serviceRequest) {
        return serviceRequest != null &&
                StringUtils.isNoneBlank(serviceRequest.getServiceUuid(), serviceRequest.getUsername());
    }

    /**
     * This fills the response with the basic information like:
     * - service's uuid executed
     * - username that requested the service
     * - date of execution, in millis
     * - parameters used
     *
     * @param serviceRequest Object holding the data needed by the service to execute.
     * @return a response partially filled
     */
    public ServiceResponse createBasicResponseOutOfRequest(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = null;
        if (serviceRequest != null) {
            serviceResponse = new ServiceResponse();
            serviceResponse.setServiceUUID(serviceRequest.getServiceUuid());
            serviceResponse.setUsername(serviceRequest.getUsername());
            serviceResponse.setExecutionDate(new Date().getTime());

            if (serviceRequest.getParameters() != null) {
                String parameters = serviceRequest.getParameters().stream().collect(Collectors.joining(","));
                serviceResponse.setUsedParameters(parameters);
            }
        }
        return serviceResponse;
    }
}
