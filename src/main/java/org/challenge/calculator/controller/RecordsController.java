package org.challenge.calculator.controller;

import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.model.AppRecord;
import org.challenge.calculator.model.AppRecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return AppRecordFactory.buildFromPageRecord(recordService.listRecords(pagingInformation));
    }
}
