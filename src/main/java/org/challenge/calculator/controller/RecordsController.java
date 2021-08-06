package org.challenge.calculator.controller;

import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.model.AppRecord;
import org.challenge.calculator.model.AppRecordFactory;
import org.hibernate.annotations.GeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
@CrossOrigin
public class RecordsController {

    private RecordService recordService;

    @Autowired
    public RecordsController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/list")
    public Page<AppRecord> listRecords(Pageable pagingInformation){
        return  AppRecordFactory.buildFromPageRecord(recordService.listRecords(pagingInformation));
    }
    /*
    @GetMapping("/list/{username}")
    public Page<AppRecord> listRecords(Pageable pagingInformation, @PathVariable(required = false) String username){
        Page<AppRecord> results;
        if (username != null) {
            results = AppRecordFactory.buildFromPageRecord(recordService.listRecords(pagingInformation, username));
        } else {
            results = AppRecordFactory.buildFromPageRecord(recordService.listRecords(pagingInformation));
        }
        return results;
    }*/
}
