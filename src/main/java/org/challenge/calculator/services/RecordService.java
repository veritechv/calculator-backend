package org.challenge.calculator.services;

import org.challenge.calculator.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService {

    /*
     * Returns a list of Records using the paging information.
     */
    Page<Record> listRecordsForAdmin(int pageIndex, int pageSize, String sortField);

    /*
     * Returns a list of Record objects corresponding for the username specified.
     */
    Page<Record> listRecords(int pageIndex, int pageSize, String sortField, String username);

    /*
     * Creates a new record
     */
    Record createRecord(Record newRecord);

    /*
     * Update the specified record.
     */
    Record updateRecord(Record record);

    /*
     * Deletes a record using it's uuid.
     */
    void deleteRecord(String recordUuid);


}
