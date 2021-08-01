package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalculatorServiceService {
    Page<Service> listServices(Pageable pagingInformation);
}
