package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;

import java.util.Date;

public abstract class CalculatorService {
    public abstract ServiceResponse execute(ServiceRequest serviceRequest);
    public boolean isRequestValid(ServiceRequest serviceRequest){
        return serviceRequest!=null &&
                StringUtils.isNoneBlank(serviceRequest.getServiceUUID(), serviceRequest.getUsername());
    }

    public ServiceResponse createBasicResponseOutOfRequest(ServiceRequest serviceRequest){
        ServiceResponse serviceResponse = null;
        if(serviceRequest!=null){
            serviceResponse = new ServiceResponse();
            serviceResponse.setServiceUUID(serviceRequest.getServiceUUID());
            serviceResponse.setUsername(serviceRequest.getUsername());
            serviceResponse.setExecutionDate(new Date().getTime());
        }

        return serviceResponse;
    }
}
