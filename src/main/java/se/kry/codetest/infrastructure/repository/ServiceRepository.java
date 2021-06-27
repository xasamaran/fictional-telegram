package se.kry.codetest.infrastructure.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import se.kry.codetest.models.service.Service;
import se.kry.codetest.models.service.ServiceDBObject;
import se.kry.codetest.models.service.factory.ServiceFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ServiceRepository {

    private final DBConnector connector;

    public ServiceRepository(DBConnector connector) {
        this.connector = connector;
    }

    public Future<Set<Service>> getAllService() {
        return this.connector.query("SELECT * FROM service ORDER BY name ASC")
                .map(resultSet ->
                        resultSet.getRows()
                                .stream()
                                .map(entries -> ServiceFactory.createService(entries.mapTo(ServiceDBObject.class)))
                                .collect(Collectors.toSet())
                );
    }

    public Future<ResultSet> addService(Service service) {
        JsonArray json = new JsonArray();
        json.add(service.getUrl());
        json.add(service.getName());
        json.add(service.getCreationDate().toString());
        json.add(false);
        return this.connector.query("INSERT INTO service values (?, ?, ?, ?)", json);
    }

    public Future<ResultSet> patchServiceAlive(Service service) {
        JsonArray json = new JsonArray();
        json.add(service.isAlive() ? 1 : 0);
        json.add(service.getName());
        return this.connector.query("UPDATE service SET alive=? WHERE name=?", json);
    }

    public Future<ResultSet> patchServiceURL(Service service) {
        JsonArray json = new JsonArray();
        json.add(service.getUrl());
        json.add(service.getName());
        return this.connector.query("UPDATE service SET url=? WHERE name=?", json);
    }

    public Future<ResultSet> deleteServiceByName(String serviceName) {
        JsonArray json = new JsonArray();
        json.add(serviceName);
        return this.connector.query("DELETE FROM service WHERE name=?", json);
    }

    public Future<Set<Service>> getService(String name) {
        JsonArray json = new JsonArray();
        json.add(name);
        return this.connector.query("SELECT * FROM service WHERE name=?", json).map(resultSet ->
                resultSet.getRows()
                        .stream()
                        .map(entries -> ServiceFactory.createService(entries.mapTo(ServiceDBObject.class)))
                        .collect(Collectors.toSet())
        );
    }
}
