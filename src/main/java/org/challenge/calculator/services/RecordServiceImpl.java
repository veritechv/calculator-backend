package org.challenge.calculator.services;

import org.challenge.calculator.entity.Record;
import org.challenge.calculator.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService{
    private RecordRepository recordRepository;

    @Autowired
    public RecordServiceImpl(RecordRepository repository) {
        this.recordRepository = repository;
    }

    @Override
    public Page<Record> listRecords(Pageable pagingInformation) {
        return recordRepository.findAll(pagingInformation);
    }
}
