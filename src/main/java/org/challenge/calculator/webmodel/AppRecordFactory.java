package org.challenge.calculator.webmodel;

import org.challenge.calculator.entity.Record;
import org.springframework.data.domain.Page;

public class AppRecordFactory {
    public static AppRecord buildFromRecord(Record record){
        AppRecord appRecord = null;
        if(record!=null){
            String serviceNameExecuted = record.getServiceExecuted()!=null?record.getServiceExecuted().getServiceName().name():null;
            String caller = record.getCaller()!=null ? record.getCaller().getUsername():null;
            long executionDate = record.getExecutionDate()!=null?record.getExecutionDate().getTime():0L;

            appRecord = new AppRecord(record.getId(), serviceNameExecuted, caller, record.getExecutionCost(),
                    record.getRemainingBalance(), record.getServiceResponse(), executionDate);
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
