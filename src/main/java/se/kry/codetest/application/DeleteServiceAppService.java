package se.kry.codetest.application;

import io.vertx.core.Future;
import io.vertx.ext.sql.ResultSet;
import se.kry.codetest.infrastructure.repository.ServiceRepository;

public class DeleteServiceAppService {

    private ServiceRepository repository;

    public DeleteServiceAppService(ServiceRepository repository) {
        this.repository = repository;
    }

    public Future<ResultSet> deleteServiceByName(String name) {
        return this.repository.deleteServiceByName(name);
    }
}
