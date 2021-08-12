package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.enums.ServiceType;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ServiceExecutorImplTest {

    private static Stream<Arguments> testExecute(){
        return Stream.of(//
                arguments(ServiceType.ADDITION, ServiceStatus.ACTIVE),
                arguments(ServiceType.ADDITION, ServiceStatus.BETA),
                arguments(ServiceType.SUBTRACTION, ServiceStatus.ACTIVE),
                arguments(ServiceType.SUBTRACTION, ServiceStatus.BETA),
                arguments(ServiceType.MULTIPLICATION, ServiceStatus.ACTIVE),
                arguments(ServiceType.MULTIPLICATION, ServiceStatus.BETA),
                arguments(ServiceType.DIVISION, ServiceStatus.ACTIVE),
                arguments(ServiceType.DIVISION, ServiceStatus.BETA),
                arguments(ServiceType.SQUARE_ROOT, ServiceStatus.ACTIVE),
                arguments(ServiceType.SQUARE_ROOT, ServiceStatus.BETA),
                arguments(ServiceType.FREE_FORM, ServiceStatus.ACTIVE),
                arguments(ServiceType.FREE_FORM, ServiceStatus.BETA),
                arguments(ServiceType.FREE_FORM, null));//<--this should fail
    }

    @ParameterizedTest
    @MethodSource
    void testExecute(ServiceType serviceType, ServiceStatus serviceStatus) {
        String serviceUuid = "serviceUuid";
        ServiceCalculatorService serviceCalculatorService = createNiceMock(ServiceCalculatorService.class);
        CalculatorService additionService = createNiceMock(CalculatorService.class);
        CalculatorService subtractionService = createNiceMock(CalculatorService.class);
        CalculatorService multiplicationService = createNiceMock(CalculatorService.class);
        CalculatorService divisionService = createNiceMock(CalculatorService.class);
        CalculatorService squareRootService = createNiceMock(CalculatorService.class);
        CalculatorService freeFormService = createNiceMock(CalculatorService.class);
        CalculatorService randomStringService = createNiceMock(CalculatorService.class);

        ServiceRequest serviceRequest = new ServiceRequest("uuid", "username", Arrays.asList("2", "1"));
        ServiceResponse dummyResponse =  new ServiceResponse();

        Service existingService = new Service(serviceUuid, "name", "description", 2, serviceType,  serviceStatus, 1);
        Optional<Service> existingServiceOptional = Optional.of(existingService);
        expect(serviceCalculatorService.searchServiceByUuid(anyString())).andReturn(existingServiceOptional);

        //add expectations depending on the service and it's stauts
        switch(serviceType){
            case ADDITION:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(additionService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case SUBTRACTION:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(subtractionService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case MULTIPLICATION:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(multiplicationService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case DIVISION:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(divisionService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case SQUARE_ROOT:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(squareRootService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case FREE_FORM:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(freeFormService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case RANDOM_STRING:
                if(serviceStatus ==ServiceStatus.ACTIVE || serviceStatus == ServiceStatus.BETA) {
                    expect(randomStringService.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            default:
                fail("We shouldn't be here.");
        }

        //initialize mocks
        replay(serviceCalculatorService, additionService, subtractionService,
                multiplicationService, divisionService, squareRootService,
                freeFormService, randomStringService);

        ServiceExecutorImpl serviceExecutor = new ServiceExecutorImpl(serviceCalculatorService,
                randomStringService, additionService, subtractionService, multiplicationService,
                divisionService, squareRootService, freeFormService);


        serviceExecutor.execute(serviceRequest);

        //Check that the expected calls were made
        verify(serviceCalculatorService, additionService, subtractionService,
                multiplicationService, divisionService, squareRootService,
                freeFormService, randomStringService);
    }
}