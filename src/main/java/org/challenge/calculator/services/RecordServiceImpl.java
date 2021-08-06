package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService{
    private UserService userService;
    private RecordRepository recordRepository;

    @Autowired
    public RecordServiceImpl(UserService userService, RecordRepository recordRepository) {
        this.userService = userService;
        this.recordRepository = recordRepository;
    }

    @Override
    public Page<Record> listRecords(Pageable pagingInformation) {
        return recordRepository.findAll(pagingInformation);
    }

    @Override
    public Page<Record> listRecords(Pageable pagingInformation, String username) {
        Page<Record> results = null;
        if(StringUtils.isNotBlank(username)){
            Optional<User> caller =  userService.searchUser(username);
            if(caller.isEmpty()){
                throw new UsernameNotFoundException("The user specified doesn't exist.");
            }
            results = recordRepository.findRecordsByUser(caller.get(), pagingInformation);
        }else{
            throw new IllegalArgumentException("The username specified is not valid");
        }
        return results;
    }

    @Override
    public Record createRecord(Record newRecord) {
        return recordRepository.save(newRecord);
    }
}
