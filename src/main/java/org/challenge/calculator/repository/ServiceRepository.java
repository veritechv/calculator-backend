package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Service;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Integer> {
}
