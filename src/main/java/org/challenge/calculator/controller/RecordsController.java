package org.challenge.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.challenge.calculator.entity.Record;
import org.challenge.calculator.model.AppRecord;
import org.challenge.calculator.model.AppRecordFactory;
import org.challenge.calculator.services.RecordService;
import org.challenge.calculator.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "calculatorapi")
@RestController
@RequestMapping("/api/v1/records")
@CrossOrigin
public class RecordsController {
    private RecordService recordService;

    @Autowired
    public RecordsController(RecordService recordService) {
        this.recordService = recordService;
    }

    @Operation(summary = "Get the list of all the service execution records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the records found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "If the pagination parameters are wrong",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping("/admin")
    public Page<AppRecord> listRecords(@Parameter(description = "Zero based index that indicates the page we want to retrieve")
                                       @RequestParam(defaultValue = "0") Integer pageIndex,
                                       @Parameter(description = "Number of records per page. Default is 20.")
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @Parameter(description = "Field name used to sort the returned elements. Default is date")
                                       @RequestParam(defaultValue = "date") String sortBy) {
        Page<AppRecord> results = AppRecordFactory.buildFromPageRecord(recordService.listRecordsForAdmin(pageIndex, pageSize, sortBy));
        return results;
    }

    @Operation(summary = "Get the list of all the user's service execution records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a paginated view of all the records found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "If the pagination parameters are wrong",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "If the username is not valid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @GetMapping("/{username}")
    public Page<AppRecord> listRecords(@Parameter(description = "username in the records")
                                       @PathVariable(name = "username") String username,
                                       @Parameter(description = "Zero based index that indicates the page we want to retrieve")
                                       @RequestParam(defaultValue = "0") Integer pageIndex,
                                       @Parameter(description = "Number of records per page. Default is 20.")
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @Parameter(description = "Field name used to sort the returned elements. Default is date")
                                       @RequestParam(defaultValue = "date") String sortBy) {
        Page<AppRecord> results = AppRecordFactory.buildFromPageRecord(recordService.listRecords(pageIndex, pageSize,
                sortBy, username));
        return results;
    }

    @Operation(summary = "Updates a record's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the record with the new data.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppRecord.class))}),
            @ApiResponse(responseCode = "404", description = "If record we want to update doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @PutMapping
    public ResponseEntity<AppRecord> updateRecord(@RequestBody AppRecord appRecord) {
        Record record = recordService.updateRecord(AppRecordFactory.buildFromAppRecord(appRecord));
        ResponseEntity<AppRecord> response =
                new ResponseEntity<>(AppRecordFactory.buildFromRecord(record), HttpStatus.OK);
        return response;
    }

    @Operation(summary = "Deletes an specific record identified by it's uuid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returs an ACK message for the deletion",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "If the record we want to delete doesn't exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})})
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteRecord(@PathVariable(name = "uuid") String recordUuid) {
        recordService.deleteRecord(recordUuid);
        ResponseEntity<String> response =
                new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("Record deleted successfully!"),
                        HttpStatus.OK);
        return response;
    }
}
