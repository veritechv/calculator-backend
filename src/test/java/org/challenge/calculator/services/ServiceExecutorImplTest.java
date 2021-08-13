package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.enums.ServiceType;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.model.ServiceRequest;
import org.challenge.calculator.model.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ServiceExecutorImplTest {
    private ServiceCalculatorService serviceCalculatorServiceMock;
    private CalculatorService additionServiceMock;
    private CalculatorService subtractionServiceMock;
    private CalculatorService multiplicationServiceMock;
    private CalculatorService divisionServiceMock;
    private CalculatorService squareRootServiceMock;
    private CalculatorService freeFormServiceMock;
    private CalculatorService randomStringServiceMock;

    @BeforeEach
    void setUp() {
        //Initialize empty mocks. Every test should define the expectations
        serviceCalculatorServiceMock = createNiceMock(ServiceCalculatorService.class);
        additionServiceMock = createNiceMock(CalculatorService.class);
        subtractionServiceMock = createNiceMock(CalculatorService.class);
        multiplicationServiceMock = createNiceMock(CalculatorService.class);
        divisionServiceMock = createNiceMock(CalculatorService.class);
        squareRootServiceMock = createNiceMock(CalculatorService.class);
        freeFormServiceMock = createNiceMock(CalculatorService.class);
        randomStringServiceMock = createNiceMock(CalculatorService.class);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> testServiceExecution() {
        return Stream.of(//
                //first set of tests, passing all current service types with allowed status. They should succeed.
                arguments(ServiceType.ADDITION, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.ADDITION, ServiceStatus.BETA, true),
                arguments(ServiceType.SUBTRACTION, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.SUBTRACTION, ServiceStatus.BETA, true),
                arguments(ServiceType.MULTIPLICATION, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.MULTIPLICATION, ServiceStatus.BETA, true),
                arguments(ServiceType.DIVISION, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.DIVISION, ServiceStatus.BETA, true),
                arguments(ServiceType.SQUARE_ROOT, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.SQUARE_ROOT, ServiceStatus.BETA, true),
                arguments(ServiceType.FREE_FORM, ServiceStatus.ACTIVE, true),
                arguments(ServiceType.FREE_FORM, ServiceStatus.BETA, true),
                //This case is to check that if configured service has a null or invalid status
                //they should fail
                arguments(ServiceType.FREE_FORM, null, false),
                arguments(ServiceType.FREE_FORM, ServiceStatus.INACTIVE, false),
                arguments(ServiceType.FREE_FORM, ServiceStatus.DELETED, false)
        );
    }

    /**
     * This test tries to determine if the correct service is called depending on the service request.
     *
     * @param serviceTypeRequested    Type of service we want to call
     * @param serviceStatusInDatabase Status of the service we want to call
     * @param shouldTestSucceed       True if the service call should succeed , false if we are expecting it to fail
     */
    @ParameterizedTest
    @MethodSource
    void testServiceExecution(ServiceType serviceTypeRequested, ServiceStatus serviceStatusInDatabase, boolean shouldTestSucceed) {
        String serviceUuid = "serviceUuid";

        ServiceRequest serviceRequest = new ServiceRequest("uuid", "username", Arrays.asList("2", "1"));
        ServiceResponse dummyResponse = new ServiceResponse();

        Service existingService = new Service(serviceUuid, "name", "description", 2, serviceTypeRequested, serviceStatusInDatabase, 1);
        Optional<Service> existingServiceOptional = Optional.of(existingService);
        expect(serviceCalculatorServiceMock.searchServiceByUuid(anyString())).andReturn(existingServiceOptional);

        //add expectations depending on the service and it's stauts
        switch (serviceTypeRequested) {
            case ADDITION:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(additionServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case SUBTRACTION:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(subtractionServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case MULTIPLICATION:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(multiplicationServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case DIVISION:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(divisionServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case SQUARE_ROOT:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(squareRootServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case FREE_FORM:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(freeFormServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            case RANDOM_STRING:
                if (serviceStatusInDatabase == ServiceStatus.ACTIVE || serviceStatusInDatabase == ServiceStatus.BETA) {
                    expect(randomStringServiceMock.execute(serviceRequest)).andReturn(dummyResponse).once();
                }
                break;
            default:
                fail("We shouldn't be here.");
        }

        //initialize mocks
        replay(serviceCalculatorServiceMock, additionServiceMock, subtractionServiceMock,
                multiplicationServiceMock, divisionServiceMock, squareRootServiceMock,
                freeFormServiceMock, randomStringServiceMock);

        ServiceExecutorImpl serviceExecutor = new ServiceExecutorImpl(serviceCalculatorServiceMock,
                randomStringServiceMock, additionServiceMock, subtractionServiceMock, multiplicationServiceMock,
                divisionServiceMock, squareRootServiceMock, freeFormServiceMock);


        try {
            serviceExecutor.execute(serviceRequest);
            assertTrue(shouldTestSucceed);//reaching this point means that the execution was OK
        } catch (CalculatorException exception) {
            assertFalse(shouldTestSucceed);//getting an exception means that we were expecting this to fail
        }

        //Check that the expected calls were made
        verify(serviceCalculatorServiceMock, additionServiceMock, subtractionServiceMock,
                multiplicationServiceMock, divisionServiceMock, squareRootServiceMock,
                freeFormServiceMock, randomStringServiceMock);
    }


    /*
        Note: We assume that the test defined the addition service, that's why we expect to fail
        whenever we pass null, 0 or 1 parameters.
     */
    @SuppressWarnings("unused")
    static private Stream<Arguments> testRequestValidation() {
        return Stream.of(
                arguments(null, false),
                arguments(new ServiceRequest(null, "username", new ArrayList<>()), false),
                arguments(new ServiceRequest("uuid", null, new ArrayList<>()), false),
                arguments(new ServiceRequest("uuid", "username", null), false),
                arguments(new ServiceRequest("uuid", "username", new ArrayList<>()), false),
                arguments(new ServiceRequest("uuid", "username", Arrays.asList("1")), false),
                arguments(new ServiceRequest("uuid", "username", Arrays.asList("1", "1")), true)
        );

    }

    /**
     * This test verify that we do the proper validations to the request.
     * @param serviceRequest Requests we want to execute
     * @param shouldTestSucceed True if the request is valid and the service was called.
     *                          False if the request is not valid and the test should fail.
     */
    @ParameterizedTest
    @MethodSource
    public void testRequestValidation(ServiceRequest serviceRequest, boolean shouldTestSucceed) {

        //setup expectations
        Service service = new Service("uuid", "Test service", "just for testing", 2, ServiceType.ADDITION, ServiceStatus.ACTIVE, 1);
        expect(serviceCalculatorServiceMock.searchServiceByUuid("uuid")).andReturn(Optional.of(service)).times(0,1);

        ServiceResponse dummyResponse = new ServiceResponse("uuid", "username", "params", "2", 100, 1L);
        expect(additionServiceMock.execute(serviceRequest)).andReturn(dummyResponse).times(0,1);

        replay(serviceCalculatorServiceMock, additionServiceMock);

        ServiceExecutorImpl serviceExecutor = new ServiceExecutorImpl(serviceCalculatorServiceMock,
                randomStringServiceMock, additionServiceMock, subtractionServiceMock, multiplicationServiceMock,
                divisionServiceMock, squareRootServiceMock, freeFormServiceMock);

        try {
            serviceExecutor.execute(serviceRequest);
            assertTrue(shouldTestSucceed);//reaching this point means that the execution was OK
        } catch (CalculatorException exception) {
            assertFalse(shouldTestSucceed);//getting an exception means that we were expecting this to fail
        }

        verify(serviceCalculatorServiceMock, additionServiceMock);
    }

}