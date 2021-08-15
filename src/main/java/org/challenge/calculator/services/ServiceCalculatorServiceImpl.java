package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.enums.ServiceType;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.repository.ServiceRepository;
import org.challenge.calculator.utils.PagingInformationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
public class ServiceCalculatorServiceImpl implements ServiceCalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCalculatorServiceImpl.class);
    private ServiceRepository serviceRepository;


    @Autowired
    public ServiceCalculatorServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Retrieves the service information using it's UUID.
     *
     * @param uuid UUID of the service we want to find.
     * @return An Optional with the service information.
     */
    @Override
    public Optional<Service> searchServiceByUuid(String uuid) {
        return serviceRepository.findByUuid(uuid);
    }

    /**
     * Method intended for admin users since it doesn't take into account the status.
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of services in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return A set of Services
     */
    @Override
    public Page<Service> listServicesForAdmin(int pageIndex, int pageSize, String sortingField) {
        return getServices(pageIndex, pageSize, sortingField, true);
    }

    /**
     * Method intented for non-admin users, since this will return Services either ACTIVE or in BETA status.
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of services in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return A set of services
     */
    @Override
    public Page<Service> listServices(int pageIndex, int pageSize, String sortingField) {
        return getServices(pageIndex, pageSize, sortingField, false);
    }

    /**
     * Method in charge of returning all the services or to filter them according to the isAdmin flag.
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of services in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @param isAdmin      Flag indicating if the results are for an admin user or other.
     * @return a set of Services
     */
    private Page<Service> getServices(int pageIndex, int pageSize, String sortingField, boolean isAdmin) {
        Page<Service> result;
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);

        try {
            List<ServiceStatus> requiredStatus;
            if (isAdmin) {
                requiredStatus = Arrays.asList(ServiceStatus.values());
            } else {
                requiredStatus = new ArrayList<>();
                requiredStatus.add(ServiceStatus.ACTIVE);
                requiredStatus.add(ServiceStatus.BETA);
            }
            result = serviceRepository.findAllByStatusIn(requiredStatus, pagingInformation);
        } catch (PropertyReferenceException exception) {
            LOGGER.info("The paging information is wrong. Please verify.");
            throw new CalculatorException("The paging information is wrong. Please verify.",
                    ErrorCause.INVALID_PARAMETERS);
        }

        return result;
    }

    /**
     * Updates the status and the cost of an existing service
     *
     * @param service The object holding the information we want to update.
     * @return The service just updated.
     * @throws CalculatorException if the service we want to update isn't found.
     */
    @Override
    public Service updateService(Service service) {
        Service existingService;
        if (service != null && StringUtils.isNotBlank(service.getUuid())) {
            Optional<Service> optionalExistingService = serviceRepository.findByUuid(service.getUuid());
            if (!optionalExistingService.isPresent()) {
                throw new CalculatorException("The service with UUID [" + service.getUuid() + "] was found",
                        ErrorCause.SERVICE_NOT_FOUND);
            }
            existingService = optionalExistingService.get();
            existingService.setName(service.getName());
            existingService.setDescription(service.getDescription());
            existingService.setType(service.getType());
            existingService.setStatus(service.getStatus());
            existingService.setStatus(service.getStatus());
            existingService.setCost(service.getCost());

            //save changes
            serviceRepository.save(optionalExistingService.get());
        } else {
            throw new CalculatorException("Service information is incorrect. Please check.",
                    ErrorCause.INVALID_PARAMETERS);
        }

        return existingService;
    }

    /**
     * This method updates the status of an existing service to DELETED
     *
     * @param serviceUuid the identifier used to find the service for deletion
     * @throws CalculatorException if the service isn't found or if serviceUuid is empty.
     */
    @Override
    public void deleteService(String serviceUuid) {
        if (StringUtils.isNotBlank(serviceUuid)) {
            Optional<Service> optionalExistingService = serviceRepository.findByUuid(serviceUuid);
            if (!optionalExistingService.isPresent()) {
                throw new CalculatorException("The service with UUID [" + serviceUuid + "] was found",
                        ErrorCause.SERVICE_NOT_FOUND);
            }

            Service existingService = optionalExistingService.get();
            //delete the service
            existingService.setStatus(ServiceStatus.DELETED);
            serviceRepository.save(existingService);
        } else {
            throw new CalculatorException("Service information is incorrect. Please check.",
                    ErrorCause.INVALID_PARAMETERS);
        }
    }

    /**
     * Creates a new service in the database.
     *
     * @param service object holding the data for the new service.
     * @return the service just created
     */
    @Override
    public Service createService(Service service) {
        Service newService;

        if (service != null && service.getType() != null && StringUtils.isNotBlank(service.getName()) &&
                service.getCost() >= 0 && service.getNumParameters() >= 0) {
            //by default the status is INACTIVE, it can be changed later
            service.setStatus(ServiceStatus.INACTIVE);
            //assigning uuid
            service.setUuid(UUID.randomUUID().toString());
            //register new service
            try {
                newService = serviceRepository.save(service);
                LOGGER.info("Service [" + newService.getName() + "] created successfully with ID[" +
                        newService.getId() + "] and UUID[" + newService.getUuid() + "], status [" +
                        newService.getStatus() + "]");
            } catch (Exception exception) {
                LOGGER.error(exception.getMessage());
                throw new CalculatorException("Service information is incorrect. Please check.",
                        ErrorCause.INVALID_PARAMETERS);
            }

        } else {
            throw new CalculatorException("Service information is incorrect. Please check.",
                    ErrorCause.INVALID_PARAMETERS);
        }
        return newService;
    }


    /**
     * Returns the values of the ServiceType enumeration.
     */
    @Override
    public List<String> getServiceTypes() {
        return Stream.of(ServiceType.values()).map(type -> {
            return type.name();
        }).collect(Collectors.toList());
    }
}
