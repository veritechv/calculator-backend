package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/*
 * A ServiceCalculatorService is in charge of the life-cycle of a service.
 * Then this interface the methods  related to retrieving and updating service data.
 */
public interface ServiceCalculatorService {
    /**
     * Get the list of services registered for regular users
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of services in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return a list of services
     */
    Page<Service> listServices(int pageIndex, int pageSize, String sortingField);

    /**
     * Get the list of all services registered in the system for admin purposes
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of services in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return a list of services
     */
    Page<Service> listServicesForAdmin(int pageIndex, int pageSize, String sortingField);


    Optional<Service> searchServiceByUuid(String Uuid);

    /**
     * Update the service's information
     *
     * @param serviceTemplate object holding the values that need to be updated
     * @return the updated service
     */
    Service updateService(Service serviceTemplate);

    /**
     * Deletes the specified service
     *
     * @param serviceUuid the identifier used to find the service for deletion
     */
    void deleteService(String serviceUuid);

    /**
     * Register a new service
     *
     * @param service object holding the data for the new service.
     * @return The service just created
     */
    Service createService(Service service);

    /**
     * Returns the list of the different types of services that can be executed.
     */
    List<String> getServiceTypes();
}
