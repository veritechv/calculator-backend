package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServiceCalculatorService {
    Page<Service> listServices(Pageable pagingInformation);
    Optional<Service> searchServiceByUuid(String Uuid);
}
