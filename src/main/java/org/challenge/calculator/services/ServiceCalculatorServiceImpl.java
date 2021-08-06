package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.controller.CalculatorController;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;

import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceCalculatorServiceImpl implements ServiceCalculatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCalculatorServiceImpl.class);
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
    public Page<Service> listServices(int pageIndex, int pageSize, String sortingField) {
        Pageable pagingInformation;
        Page<Service> result;
        if (StringUtils.isNotBlank(sortingField)) {
            pagingInformation = PageRequest.of(pageIndex, pageSize, Sort.by(sortingField));
        } else {
            pagingInformation = PageRequest.of(pageIndex, pageSize, Sort.by(sortingField));
        }

        try {
            result = serviceRepository.findAll(pagingInformation);
        } catch (PropertyReferenceException exception) {
            LOGGER.info("The paging information is wrong. Please verify. Returning empty results");
            result = Page.empty();
        }

        return result;
    }
}
