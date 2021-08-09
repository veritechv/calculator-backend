package org.challenge.calculator.model;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Service;
import org.challenge.calculator.enums.ServiceType;
import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.utils.SanitizeUtil;
import org.springframework.data.domain.Page;

public class AppServiceFactory {

    public static AppService buildFromService(Service service) {
        AppService appService = null;
        if (service != null) {
            appService = new AppService(service.getUuid(), service.getName(), service.getDescription(),
                    service.getStatus().name(), service.getType().name(),
                    service.getNumParameters(), service.getCost());
        }

        return appService;
    }

    static public Page<AppService> buildFromPageService(Page<Service> pageToTransform) {
        Page<AppService> transformedPage = null;
        if (pageToTransform != null) {
            transformedPage = pageToTransform.map(AppServiceFactory::buildFromService);
        }
        return transformedPage;
    }

    /**
    This method will try to build a Service object out of an AppService one.
     If the status comes empty we assign INACTIVE by default

     @param appService object that we want to convert into a Service object.
     @return A Service object initialized with appService's values, not including the id attribute.
             We can get IllegalArgumentExceptions when trying to convert the service name
             and status if we pass a name/status that is not in ServiceType/ServiceStatus
             enumeration.
     */
    static public Service buildFromAppService(AppService appService) {
        Service service = null;
        if (appService != null) {
            service = new Service(SanitizeUtil.sanitize(appService.getUuid()),
                    SanitizeUtil.sanitize(appService.getName()),
                    SanitizeUtil.sanitize(appService.getDescription()),
                    appService.getNumParameters(),
                    ServiceType.valueOf(appService.getType()),
                    ServiceStatus.valueOf(StringUtils.defaultIfBlank(appService.getStatus(), ServiceStatus.INACTIVE.name())),
                    appService.getCost());
        }
        return service;
    }
}
