package org.challenge.calculator.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.services.ServiceCalculatorService;
import org.challenge.calculator.services.UserService;
import org.challenge.calculator.utils.ServiceUsageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * This interceptor is to handle common functionality among the services.
 */
@Aspect
@Component
public class ServiceRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestInterceptor.class);
    private UserService userService;
    private ServiceCalculatorService calculatorServiceService;
    private RecordService recordService;

    @Autowired
    public ServiceRequestInterceptor(UserService userService, ServiceCalculatorService calculatorServiceService, RecordService recordService) {
        this.userService = userService;
        this.calculatorServiceService = calculatorServiceService;
        this.recordService = recordService;
    }

    /**
     * This pointcut is in charge of:
     * - checking the user's balance before executing the service.
     * - update the user's balance after the successful service execution.
     * @param jp the method, arguments and all the information related to the service execution
     * @return The result of the service's method call
     * @throws Throwable if something unexpected happens.
     * @throws CalculatorException If the user's balance is not enough.
     */
    @Around("execution(* org.challenge.calculator.services.CalculatorService.execute*(..)) && !target(org.challenge.calculator.services.ServiceExecutorImpl)")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        String serviceName = jp.getSignature().getDeclaringType().getSimpleName();
        LOGGER.info("Validating service request for [].[" + methodName + "] in service [" + serviceName + "]");

        Object result = null;

        if (jp.getArgs().length > 0 && (jp.getArgs()[0] instanceof ServiceRequest)) {
            ServiceRequest serviceRequest = (ServiceRequest) jp.getArgs()[0];
            if (serviceRequest != null && StringUtils.isNoneBlank(serviceRequest.getServiceUuid(), serviceRequest.getUsername())) {
                Optional<User> userOptional = userService.searchUser(serviceRequest.getUsername());
                Optional<Service> serviceOptional =
                        calculatorServiceService.searchServiceByUuid(serviceRequest.getServiceUuid());

                if (userOptional.isPresent() && serviceOptional.isPresent()) {
                    if (ServiceUsageCalculator.isBalanceEnough(userOptional.get(), serviceOptional.get())) {
                        result = jp.proceed();
                        if (result != null) {
                            long newBalance = updateBalance(userOptional.get(), serviceOptional.get());
                            //update response with new balance
                            ((ServiceResponse) result).setRemainingBalance(newBalance);
                            createRecord(userOptional.get(), serviceOptional.get(), newBalance,
                                    ((ServiceResponse) result).getResponse());
                        }
                    } else {
                        throw new CalculatorException("Inssuficiente balance in user's account. " +
                                "Couldn't execute [" + serviceName + "]", ErrorCause.INSUFFICIENT_BALANCE);
                    }
                }
            }

        } else {
            throw new IllegalArgumentException("Service request not valid.");
        }
        LOGGER.info("returning result");
        return result;
    }

    /**
     * Update user's balance after service execution
     * @param caller the user who requested the service
     * @param service the service executed
     * @return the balance after the deduction of the service's cost
     */
    private long updateBalance(User caller, Service service){
        long newBalance = ServiceUsageCalculator.calculateNewBalance(caller, service);
        //update user
        caller.setBalance(newBalance);
        userService.updateUser(caller);
        return newBalance;
    }

    /**
     * Creates a new record with the information of the service execution.
     * By default the date is now()
     * @param caller Who requestedthe service
     * @param service The service executed
     * @param remainingBalance The balance after taking out the  execution's cost
     * @param response The result of the service execution.
     * @return The record just created.
     */
    private Record createRecord(User caller, Service service, long remainingBalance, String response){
        Record record = new Record(service, caller, service.getCost(), remainingBalance, response, new Date());
        return recordService.createRecord(record);
    }

}
