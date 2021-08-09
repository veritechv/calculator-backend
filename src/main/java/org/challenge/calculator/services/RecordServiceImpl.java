package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.controller.UsersController;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.RecordNotFoundException;
import org.challenge.calculator.repository.RecordRepository;
import org.challenge.calculator.utils.PagingInformationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public Page<Record> listRecords(int pageIndex, int pageSize, String sortingField, String username) {
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);
        Page<Record> results = null;
        if (StringUtils.isNotBlank(username)) {
            Optional<User> caller = userService.searchUser(username);
            if (caller.isEmpty()) {
                throw new UsernameNotFoundException("The user specified doesn't exist.");
            }
            results = recordRepository.findRecordsByUser(caller.get(), pagingInformation);
        } else {
            throw new IllegalArgumentException("The username specified is not valid");
        }
        return results;
    }

    @Override
    public Record createRecord(Record newRecord) {
        return recordRepository.save(newRecord);
    }

    /**
     * Update the fields: cost, balance and/or response of an existing record
     *
     * @param record record that we want to update
     * @return the record just updated
     * @throws RecordNotFoundException  if we couldn't find the record using it's uuid
     * @throws IllegalArgumentException if the record information is incomplete or invalid
     */
    public Record updateRecord(Record record) {
        if (record != null || StringUtils.isNoneBlank(record.getUuid(), record.getResponse())) {
            Optional<Record> existingRecordOptional = recordRepository.findRecordByUuid(record.getUuid());

            if (existingRecordOptional.isEmpty()) {
                LOGGER.error("We couldn't find the record with UUID [" + record.getUuid() + "]");
                throw new RecordNotFoundException("We couldn't find the record with UUID [" + record.getUuid() + "]");
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
     * @param recordUuid UUID of the record we want to delete
     */
    @Override
    public void deleteRecord(String recordUuid) {
        if(StringUtils.isNotBlank(recordUuid)){
            Optional<Record> existingRecordOptional = recordRepository.findRecordByUuid(recordUuid);
            if (existingRecordOptional.isEmpty()) {
                LOGGER.error("We couldn't find the record with UUID [" + recordUuid + "]");
                throw new RecordNotFoundException("We couldn't find the record with UUID [" + recordUuid + "]");
            }
            recordRepository.delete(existingRecordOptional.get());
            LOGGER.info("Record with UUID["+recordUuid+"] deleted successfully!");
        }
    }
}
