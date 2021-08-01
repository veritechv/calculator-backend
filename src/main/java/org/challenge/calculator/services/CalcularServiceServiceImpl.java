package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@org.springframework.stereotype.Service
public class CalcularServiceServiceImpl implements CalculatorServiceService{

    private ServiceRepository serviceRepository;

    @Autowired
    public CalcularServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<Service> listServices(Pageable pagingInformation) {
        return serviceRepository.findAll(pagingInformation);
    }
}
