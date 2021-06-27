package se.kry.codetest.models.service.factory;

import se.kry.codetest.models.service.Service;
import se.kry.codetest.models.service.ServiceDBObject;

import java.time.LocalDate;

public class ServiceFactory {

    public static Service createService(ServiceDBObject serviceDBObject) {
        return new Service(serviceDBObject.getName(),
                serviceDBObject.getUrl(),
                LocalDate.from(serviceDBObject.getLocalDate()),
                serviceDBObject.getAlive() == 1);
    }
}
