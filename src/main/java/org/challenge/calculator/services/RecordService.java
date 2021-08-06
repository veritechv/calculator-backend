package org.challenge.calculator.services;

import org.challenge.calculator.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService {

    Page<Record> listRecords(Pageable pagingInformation);
    Page<Record> listRecords(Pageable pagingInformation, String username);
    Record createRecord(Record newRecord);

}
