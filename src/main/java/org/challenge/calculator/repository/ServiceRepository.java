package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Service;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Integer> {
    Optional<Service> findByUuid(String uuid);
}
