package org.challenge.calculator.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.InsufficientBalanceForExecution;
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

    @Around("execution(* org.challenge.calculator.services.CalculatorService.execute*(..))")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        String serviceName = jp.getSignature().getDeclaringType().getSimpleName();
        LOGGER.info("Validating service request for [].[" + methodName + "] in service [" + serviceName + "]");

        Object result = null;

        if (jp.getArgs().length > 0 && (jp.getArgs()[0] instanceof ServiceRequest)) {
            ServiceRequest serviceRequest = (ServiceRequest) jp.getArgs()[0];
            if (serviceRequest != null && StringUtils.isNoneBlank(serviceRequest.getServiceUUID(), serviceRequest.getUsername())) {
                Optional<User> userOptional = userService.searchUser(serviceRequest.getUsername());
                Optional<Service> serviceOptional =
                        calculatorServiceService.searchServiceByUuid(serviceRequest.getServiceUUID());

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
                        throw new InsufficientBalanceForExecution("Inssuficiente balance in user's account. " +
                                "Couldn't execute [" + serviceName + "]");
                    }
                }
            }

        } else {
            throw new IllegalArgumentException("Service request not valid.");
        }
        return result;
    }

    private long updateBalance(User caller, Service service){
        long newBalance = ServiceUsageCalculator.calculateNewBalance(caller, service);
        //update user
        caller.setBalance(newBalance);
        userService.updateUser(caller);
        return newBalance;
    }

    private Record createRecord(User caller, Service service, long remainingBalance, String response){
        return recordService.createRecord(new Record(service, caller, service.getCost(),
                remainingBalance, response, new Date()));
    }

}
