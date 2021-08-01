package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Record;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends PagingAndSortingRepository<Record, Long> {
}
