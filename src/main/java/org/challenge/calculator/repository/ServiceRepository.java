package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Integer> {
    Optional<Service> findByUuid(String uuid);
    Page<Service> findAllByStatusIn(List<ServiceStatus> status, Pageable pageable);
}
