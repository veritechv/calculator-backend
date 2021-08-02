package org.challenge.calculator.services;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceCalculatorServiceImpl implements ServiceCalculatorService {

    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceCalculatorServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Optional<Service> searchServiceByUuid(String uuid) {
        return serviceRepository.findByUuid(uuid);
    }

    @Override
    public Page<Service> listServices(Pageable pagingInformation) {
        return serviceRepository.findAll(pagingInformation);
    }
}
