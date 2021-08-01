package org.challenge.calculator.controller;

import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.webmodel.AppRecord;
import org.challenge.calculator.webmodel.AppRecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordsController {

    private RecordService recordService;

    @Autowired
    public RecordsController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/records/list")
    public Page<AppRecord> listRecords(Pageable pagingInformation){
        return AppRecordFactory.buildFromPageRecord(recordService.listRecords(pagingInformation));
    }
}
