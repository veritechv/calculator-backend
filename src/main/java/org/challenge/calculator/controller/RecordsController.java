package org.challenge.calculator.controller;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.exception.RecordNotFoundException;
import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.model.AppRecord;
import org.challenge.calculator.model.AppRecordFactory;
import org.challenge.calculator.services.RecordServiceImpl;
import org.challenge.calculator.utils.JsonUtil;
import org.hibernate.annotations.GeneratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordsController.class);
    private RecordService recordService;

    @Autowired
    public RecordsController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/admin")
    public Page<AppRecord> listRecords(@RequestParam(defaultValue = "0") Integer pageIndex,
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @RequestParam(defaultValue = "date") String sortBy) {
        Page<AppRecord> results = AppRecordFactory.buildFromPageRecord(recordService.listRecordsForAdmin(pageIndex, pageSize, sortBy));
        return results;
    }

    @GetMapping("/{username}")
    public Page<AppRecord> listRecords(@PathVariable(name = "username") String username,
                                       @RequestParam(defaultValue = "0") Integer pageIndex,
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @RequestParam(defaultValue = "date") String sortBy) {
        Page<AppRecord> results = null;
        if (username != null) {
            try {
                results = AppRecordFactory.buildFromPageRecord(recordService.listRecords(pageIndex, pageSize, sortBy, username));
            } catch (UsernameNotFoundException | IllegalArgumentException exception) {
                LOGGER.error(exception.getMessage());
                LOGGER.error("Returning empty list.");
                results = Page.empty();
            }
        } else {
            LOGGER.error("Missing username. Returning empty list");
        }
        return results;
    }

    @PutMapping
    public ResponseEntity<AppRecord> updateRecord(@RequestBody AppRecord appRecord) {
        ResponseEntity<AppRecord> response;

        if (appRecord != null) {
            try {
                Record record = recordService.updateRecord(AppRecordFactory.buildFromAppRecord(appRecord));
                if (record != null) {
                    response = new ResponseEntity<>(AppRecordFactory.buildFromRecord(record), HttpStatus.OK);
                } else {
                    response = new ResponseEntity("Update failed. Please verify data.", HttpStatus.BAD_REQUEST);
                }
            } catch (RecordNotFoundException | IllegalArgumentException exception) {
                response = new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            response = new ResponseEntity("Update failed. Invalid data", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * This endpoints receives requests to delete an specific record
     *
     * @param recordUuid the uuid of the record we want to delete
     * @return an status string
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteRecord(@PathVariable(name = "uuid") String recordUuid) {
        ResponseEntity<String> response;
        if (StringUtils.isNotBlank(recordUuid)) {
            try {
                recordService.deleteRecord(recordUuid);
                response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("Record deleted successfully!"), HttpStatus.OK);
            } catch (RecordNotFoundException exception) {
                response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
            }

        } else {
            response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("Invalid data. Please verify"), HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
