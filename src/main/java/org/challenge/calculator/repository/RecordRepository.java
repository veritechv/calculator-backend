package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends PagingAndSortingRepository<Record, Long> {
    Page<Record> findRecordsByUser(User user, Pageable pagingInformation);
}
