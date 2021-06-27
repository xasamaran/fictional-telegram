package se.kry.codetest.application;

import io.vertx.core.Future;
import se.kry.codetest.infrastructure.repository.ServiceRepository;
import se.kry.codetest.models.service.Service;

import java.util.Set;

public class ServiceAppService {

    private ServiceRepository repository;

    public ServiceAppService(ServiceRepository repository) {
        this.repository = repository;
    }

    public Future<Set<Service>> getAllService() {
        return repository.getAllService();
    }
}
