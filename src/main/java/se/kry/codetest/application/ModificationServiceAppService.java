package se.kry.codetest.application;

import io.vertx.core.Future;
import se.kry.codetest.infrastructure.repository.ServiceRepository;
import se.kry.codetest.models.service.Service;
import se.kry.codetest.models.service.validators.ServiceValidator;

public class ModificationServiceAppService {

    private ServiceRepository repository;
    private ServiceValidator serviceValidator;

    public ModificationServiceAppService(ServiceRepository repository, ServiceValidator serviceValidator) {
        this.repository = repository;
        this.serviceValidator = serviceValidator;
    }

    public Future<Integer> createOrUpdate(Service service) {
        this.serviceValidator.validate(service);
        return repository.getService(service.getName()).compose(event -> {
            if (event.isEmpty()) {
                return repository.addService(service).map(resultSet -> 201);
            } else {
                return repository.patchServiceURL(service).map(resultSet -> 200);
            }
        });
    }
}
