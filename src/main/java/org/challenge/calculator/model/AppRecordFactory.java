package org.challenge.calculator.model;

import org.challenge.calculator.entity.Record;
import org.springframework.data.domain.Page;

public class AppRecordFactory {
    public static AppRecord buildFromRecord(Record record){
        AppRecord appRecord = null;
        if(record!=null){
            String serviceNameExecuted = record.getService()!=null?record.getService().getName().name():null;
            String caller = record.getUser()!=null ? record.getUser().getUsername():null;
            long executionDate = record.getExecutionDate()!=null?record.getExecutionDate().getTime():0L;

            appRecord = new AppRecord(record.getUuid(), serviceNameExecuted, caller, record.getCost(),
                    record.getBalance(), record.getResponse(), executionDate);
        }

        return appRecord;
    }

    static public Page<AppRecord> buildFromPageRecord(Page<Record> pageToTransform){
        Page<AppRecord> transformedPage = null;
        if(pageToTransform!=null){
            transformedPage = pageToTransform.map(AppRecordFactory::buildFromRecord);
        }
        return transformedPage;
    }
}
