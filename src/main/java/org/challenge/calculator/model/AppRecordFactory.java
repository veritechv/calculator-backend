package org.challenge.calculator.model;

import org.challenge.calculator.entity.Record;
import org.springframework.data.domain.Page;

/**
 * Utility class to transform model objects to DTOs
 * This is utility is specific for Record objects.
 */
public class AppRecordFactory {

    /**
     * Creates a Record object into a AppRecord one
     * @param record object to transform
     * @return an AppRecord based on record
     */
    public static AppRecord buildFromRecord(Record record) {
        AppRecord appRecord = null;
        if (record != null) {
            String serviceType = record.getService() != null ? record.getService().getType().name() : null;
            String serviceName = record.getService() != null ? record.getService().getName() :  null;
            String caller = record.getUser() != null ? record.getUser().getUsername() : null;
            long executionDate = record.getDate() != null ? record.getDate().getTime() : 0L;

            appRecord = new AppRecord(record.getUuid(), serviceName, serviceType, caller, record.getCost(),
                    record.getBalance(), record.getResponse(), executionDate);
        }

        return appRecord;
    }

    /**
     * Creates a Record object base on appRecord's data
     * @param appRecord the object used as template for a new Record
     * @return a Record object
     */
    public static Record buildFromAppRecord(AppRecord appRecord){
        Record record = null;
        if(appRecord!=null){
            record = new Record();
            record.setCost(appRecord.getCost());
            record.setBalance(appRecord.getBalance());
            record.setResponse(appRecord.getResponse());
        }

        return record;
    }

    /**
     * Transforms paged list of Record objects into a Paged list of AppRecord objects
     * @param pageToTransform list of Record objects
     * @return a list of AppRecord objects
     */
    static public Page<AppRecord> buildFromPageRecord(Page<Record> pageToTransform) {
        Page<AppRecord> transformedPage = null;
        if (pageToTransform != null) {
            transformedPage = pageToTransform.map(AppRecordFactory::buildFromRecord);
        }
        return transformedPage;
    }
}
