package se.kry.codetest.application;

import io.vertx.core.Future;
import io.vertx.ext.sql.ResultSet;
import se.kry.codetest.infrastructure.repository.ServiceRepository;
import se.kry.codetest.models.service.Service;
import se.kry.codetest.models.service.validators.ServiceValidator;

public class UpdateServiceAppService {

    private ServiceRepository repository;
    private ServiceValidator serviceValidator;

    public UpdateServiceAppService(ServiceRepository repository, ServiceValidator serviceValidator) {
        this.repository = repository;
        this.serviceValidator = serviceValidator;
    }

    public Future<ResultSet> update(Service service) {
        this.serviceValidator.validate(service);
        return repository.addService(service);
    }

}
