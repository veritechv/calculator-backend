package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.enums.ServiceType;
import org.challenge.calculator.exception.ServiceNotFoundException;
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

    @Override
    public Optional<Service> searchServiceByUuid(String uuid) {
        return serviceRepository.findByUuid(uuid);
    }

    @Override
    public Page<Service> listServicesForAdmin(int pageIndex, int pageSize, String sortingField) {
        return getServices(pageIndex, pageSize, sortingField, true);
    }

    @Override
    public Page<Service> listServices(int pageIndex, int pageSize, String sortingField) {
        return getServices(pageIndex, pageSize, sortingField, false);
    }

    private Page<Service> getServices(int pageIndex, int pageSize, String sortingField, boolean isAdmin) {
        Page<Service> result;
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);

        try {
            List<ServiceStatus> requiredStatus;
            if(isAdmin){
                requiredStatus = Arrays.asList(ServiceStatus.values());
            }else{
                requiredStatus = new ArrayList<>();
                requiredStatus.add(ServiceStatus.ACTIVE);
                requiredStatus.add(ServiceStatus.BETA);
            }
            result = serviceRepository.findAllByStatusIn(requiredStatus, pagingInformation);
        } catch (PropertyReferenceException exception) {
            LOGGER.info("The paging information is wrong. Please verify. Returning empty results");
            result = Page.empty();
        }

        return result;
    }

    @Override
    public Service updateService(Service service){
        Service existingService;
        if(service!=null && StringUtils.isNotBlank(service.getUuid())){
            Optional<Service> optionalExistingService = serviceRepository.findByUuid(service.getUuid());
            if(optionalExistingService.isEmpty()){
                throw new ServiceNotFoundException("The service with UUID ["+service.getUuid()+"] was found");
            }
            existingService = optionalExistingService.get();
            //updating cost and status only
            existingService.setStatus(service.getStatus());
            existingService.setCost(service.getCost());

            //save changes
            serviceRepository.save(optionalExistingService.get());
        }else{
            throw new IllegalArgumentException("Service information is incorrect. Please check.");
        }

        return existingService;
    }

    @Override
    public void deleteService(String serviceUuid) {
        if(StringUtils.isNotBlank(serviceUuid)){
            Optional<Service> optionalExistingService = serviceRepository.findByUuid(serviceUuid);
            if(optionalExistingService.isEmpty()){
                throw new ServiceNotFoundException("The service with UUID ["+serviceUuid+"] was found");
            }

            Service existingService = optionalExistingService.get();
            //delete the service
            serviceRepository.delete(existingService);
        }else{
            throw new IllegalArgumentException("Service information is incorrect. Please check.");
        }
    }

    @Override
    public Service createService(Service service) {
        Service newService = null;

        if(service!=null && service.getType()!=null && StringUtils.isNotBlank(service.getName()) &&
                service.getCost()>=0 && service.getNumParameters()>=0){
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
            }catch(Exception exception){
                LOGGER.error(exception.getMessage());
                throw new IllegalArgumentException("Service information is incorrect. Please check.");
            }

        }else{
            throw new IllegalArgumentException("Service information is incorrect. Please check.");
        }

        return newService;
    }


    @Override
    public List<String> getServiceTypes() {
        return Stream.of(ServiceType.values()).map(type->{ return type.name();}).collect(Collectors.toList());
    }
}
