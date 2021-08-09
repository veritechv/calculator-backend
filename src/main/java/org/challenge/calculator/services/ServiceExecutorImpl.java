package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.exception.CalculatorOperationException;
import org.challenge.calculator.exception.ServiceNotFoundException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * The intention of this service is to determine based on the type which
 * service implementation to execute
 */
@org.springframework.stereotype.Service("serviceExecutor")
public class ServiceExecutorImpl extends CalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExecutorImpl.class);
    private ServiceCalculatorServiceImpl serviceCalculatorService;

    private CalculatorService randomStringService;
    private CalculatorService additionService;
    private CalculatorService subtractionService;
    private CalculatorService multiplicationService;
    private CalculatorService divisionService;
    private CalculatorService squareRootService;
    private CalculatorService freeFormService;

    @Autowired
    public ServiceExecutorImpl(ServiceCalculatorServiceImpl serviceCalculatorService,
                               CalculatorService randomStringService,
                               CalculatorService additionService,
                               CalculatorService subtractionService,
                               CalculatorService multiplicationService,
                               CalculatorService divisionService,
                               CalculatorService squareRootService,
                               CalculatorService freeFormService) {
        this.serviceCalculatorService = serviceCalculatorService;
        this.randomStringService = randomStringService;
        this.additionService = additionService;
        this.subtractionService = subtractionService;
        this.multiplicationService = multiplicationService;
        this.divisionService = divisionService;
        this.squareRootService = squareRootService;
        this.freeFormService = freeFormService;
    }

    /**
     * This is like a strategy pattern, where we try to determine the service to execute according to
     * the service request's data.
     *
     * @param serviceRequest Object holding the info about the service to execute, parameters and the calling  user
     * @return The result of the execution.
     * @throws ServiceNotFoundException if the uuid passed in the request doesn't correspond to registered service
     * @throws CalculatorOperationException if:
     * - the service is INACTIVE
     * - the number of parameters is not correct
     * - the type of service is not supported yet
     * - the request data is incomplete
     */
    @Override
    public ServiceResponse execute(ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse;

        if(isRequestValid(serviceRequest)){
            //check if the service we want to execute exists
            Optional<Service> existingServiceOptional =
                    serviceCalculatorService.searchServiceByUuid(serviceRequest.getServiceUuid());

            if(existingServiceOptional.isEmpty()){
                LOGGER.error("Couldn't find the service to execute with the UUID["+ serviceRequest.getServiceUuid()+"]");
                throw new ServiceNotFoundException("Couldn't find the service with the specified data.");
            }
            Service existingService = existingServiceOptional.get();
            //check if the service is not inactive
            if(existingService.isInactive()){
                LOGGER.error("We can't execute a service that is inactive.");
                throw new CalculatorOperationException("We can't execute a service that is inactive.");
            }

            //check if the number of parameters received is the same as the number we expect
            int serviceRequestNumParameters = serviceRequest.getParameters() == null ? 0 : serviceRequest.getParameters().size();
            if(serviceRequestNumParameters != existingService.getNumParameters()){
                LOGGER.error("Wrong number of parameters.");
                throw new CalculatorOperationException("Wrong number of parameters");
            }

            //determine which service we should execute
            switch(existingService.getType()){
                case ADDITION:
                    serviceResponse = additionService.execute(serviceRequest);
                    break;
                case SUBTRACTION:
                    serviceResponse = subtractionService.execute(serviceRequest);
                    break;
                case DIVISION:
                    serviceResponse = divisionService.execute(serviceRequest);
                    break;
                case MULTIPLICATION:
                    serviceResponse = multiplicationService.execute(serviceRequest);
                    break;
                case SQUARE_ROOT:
                    serviceResponse = squareRootService.execute(serviceRequest);
                    break;
                case FREE_FORM:
                    serviceResponse = freeFormService.execute(serviceRequest);
                    break;
                case RANDOM_STRING:
                    serviceResponse = randomStringService.execute(serviceRequest);
                    break;
                default:
                    throw new CalculatorOperationException("Service type not supported ["+existingService.getType()+"]");
            }

        }else{
            throw new CalculatorOperationException("Invalid data to execute the service.");
        }

        return serviceResponse;
    }
}
