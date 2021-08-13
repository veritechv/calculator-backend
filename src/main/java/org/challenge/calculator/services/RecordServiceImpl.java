package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.repository.RecordRepository;
import org.challenge.calculator.utils.PagingInformationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordServiceImpl.class);

    private UserService userService;
    private RecordRepository recordRepository;

    @Autowired
    public RecordServiceImpl(UserService userService, RecordRepository recordRepository) {
        this.userService = userService;
        this.recordRepository = recordRepository;
    }

    /**
     * This method if for admin purposes because it returns the list of all the records
     * in the application.
     *
     * @param pageIndex    Page number we want to retrieve
     * @param pageSize     Number of elements expected in the page.
     * @param sortingField Name of the field we should use to order the results.
     * @return A set of records.
     */
    @Override
    public Page<Record> listRecordsForAdmin(int pageIndex, int pageSize, String sortingField) {
        Page<Record> result;
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);
        try {
            result = recordRepository.findAll(pagingInformation);
        } catch (
                PropertyReferenceException exception) {
            LOGGER.info("The paging information is wrong. Please verify. Returning empty results");
            result = Page.empty();
        }
        return result;
    }

    /**
     * This method is for user's use. This returns only the records of a specific user.
     *
     * @param pageIndex    Page number we want to retrieve
     * @param pageSize     Number of elements expected in the page.
     * @param sortingField Name of the field we should use to order the results.
     * @param username     the username linked to the records
     * @return A set of records.
     */
    @Override
    public Page<Record> listRecords(int pageIndex, int pageSize, String sortingField, String username) {
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);
        Page<Record> results = null;
        if (StringUtils.isNotBlank(username)) {
            Optional<User> caller = userService.searchUser(username);
            if (!caller.isPresent()) {
                throw new CalculatorException("The user specified doesn't exist.", ErrorCause.USER_NOT_FOUND);
            }
            results = recordRepository.findRecordsByUser(caller.get(), pagingInformation);
        } else {
            throw new CalculatorException("The username specified is not valid", ErrorCause.USER_NOT_FOUND);
        }
        return results;
    }

    /**
     * Creates a new record in the database
     *
     * @param newRecord object holding the data to be saved in the database
     * @return the record just created.
     */
    @Override
    public Record createRecord(Record newRecord) {
        return recordRepository.save(newRecord);
    }

    /**
     * Update the fields: cost, balance and/or response of an existing record
     *
     * @param record record that we want to update
     * @return the record just updated
     * @throws CalculatorException if we couldn't find the record using it's uuid
     *                             or if the record information is incomplete/ invalid.
     */
    public Record updateRecord(Record record) {
        if (record != null || StringUtils.isNoneBlank(record.getUuid(), record.getResponse())) {
            Optional<Record> existingRecordOptional = recordRepository.findRecordByUuid(record.getUuid());

            if (!existingRecordOptional.isPresent()) {
                LOGGER.error("We couldn't find the record with UUID [" + record.getUuid() + "]");
                throw new CalculatorException("We couldn't find the record with UUID [" + record.getUuid() + "]",
                        ErrorCause.RECORD_NOT_FOUND);
            }
            Record existingRecord = existingRecordOptional.get();
            //update only cost, balance and response
            existingRecord.setBalance(record.getBalance());
            existingRecord.setCost(record.getCost());
            existingRecord.setResponse(record.getResponse());

            recordRepository.save(existingRecord);
            record = existingRecord;

        } else {
            LOGGER.error("Insufficient information to delete record.");
            throw new IllegalArgumentException("Insufficient information to delete record.");
        }

        return record;
    }

    /**
     * Deletes a Record from the database
     *
     * @param recordUuid UUID of the record we want to delete
     */
    @Override
    public void deleteRecord(String recordUuid) {
        if (StringUtils.isNotBlank(recordUuid)) {
            Optional<Record> existingRecordOptional = recordRepository.findRecordByUuid(recordUuid);
            if (!existingRecordOptional.isPresent()) {
                LOGGER.error("We couldn't find the record with UUID [" + recordUuid + "]");
                throw new CalculatorException("We couldn't find the record with UUID [" + recordUuid + "]",
                        ErrorCause.RECORD_NOT_FOUND);
            }
            recordRepository.delete(existingRecordOptional.get());
            LOGGER.info("Record with UUID[" + recordUuid + "] deleted successfully!");
        }
    }
}
