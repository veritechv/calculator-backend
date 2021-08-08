package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ServiceCalculatorService {
    Page<Service> listServices(int pageIndex, int pageSize, String sortingField);
    Optional<Service> searchServiceByUuid(String Uuid);

    /**
     * Update the service's information
     * @param serviceTemplate object holding the values that need to be updated
     * @return the updated service
     */
    Service updateService(Service serviceTemplate);

    /**
     * Deletes the specified service
     * @param serviceUuid the identifier used to find the service for deletion
     */
    void deleteService(String serviceUuid);

    /**
     * Register a new service
     * @param service object holding the data for the new service.
     * @return The service just created
     */
    Service createService(Service service);

    List<String> getServiceTypes();
}
