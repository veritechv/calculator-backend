package org.challenge.calculator.model;

import org.challenge.calculator.entity.Service;
import org.springframework.data.domain.Page;

public class AppServiceFactory {

    public static AppService buildFromService(Service service){
        AppService appService = null;
        if(service!=null){
            appService = new AppService(service.getUuid(), service.getServiceName().name(),
                    service.getServiceStatusName().name(), service.getCost());
        }

        return appService;
    }

    static public Page<AppService> buildFromPageService(Page<Service> pageToTransform){
        Page<AppService> transformedPage = null;
        if(pageToTransform!=null){
            transformedPage = pageToTransform.map(AppServiceFactory::buildFromService);
        }
        return transformedPage;
    }
}
