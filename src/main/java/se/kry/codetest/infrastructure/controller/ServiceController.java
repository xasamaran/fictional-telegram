package se.kry.codetest.infrastructure.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import se.kry.codetest.application.ModificationServiceAppService;
import se.kry.codetest.application.DeleteServiceAppService;
import se.kry.codetest.application.ServiceAppService;
import se.kry.codetest.models.service.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceController implements Controller {

    private Router router;
    private ServiceAppService serviceAppService;
    private ModificationServiceAppService creationService;
    private DeleteServiceAppService deleteService;

    public ServiceController(Router router,
                             ServiceAppService serviceAppService,
                             ModificationServiceAppService creationService,
                             DeleteServiceAppService deleteService) {
        this.router = router;
        this.serviceAppService = serviceAppService;
        this.creationService = creationService;
        this.deleteService = deleteService;
        this.startRoutes();
    }


    private void startRoutes() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
        router.get("/service").handler(this::handleGet);
        router.post("/service").handler(this::handlePost);
        router.delete("/service").handler(this::handleDelete);
    }

    private void handleGet(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        serviceAppService.getAllService().setHandler(res -> {
                    if (res.succeeded()) {
                        List<JsonObject> jsonServices =
                                res.result().stream()
                                        .map(JsonObject::mapFrom)
                                        .collect(Collectors.toList());
                        response.putHeader("content-type", "application/json")
                                .end(new JsonArray(jsonServices).encode());
                    } else {
                        sendError(404, response);
                    }
                }
        );
    }

    private void handlePost(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        Service service = routingContext.getBodyAsJson().mapTo(Service.class);
        if (service == null) {
            sendError(400, response);
            return;
        }
        try {
            creationService.createOrUpdate(service).setHandler(res -> {
                        if (res.succeeded()) {
                            response.putHeader("content-type", "application/json")
                                    .setStatusCode(res.result())
                                    .end();
                        } else {
                            sendError(400, response);
                        }
                    }
            );
        } catch (IllegalArgumentException e) {
            sendError(400, response);
        }
    }


    private void handleDelete(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        if (routingContext.queryParam("name") == null || routingContext.queryParam("name").isEmpty()) {
            sendError(400, response);
            return;
        }
        this.deleteService.deleteServiceByName(routingContext.queryParam("name").get(0)).setHandler(res -> {
                    if (res.succeeded()) {
                        response.putHeader("content-type", "application/json")
                                .setStatusCode(202)
                                .end();
                    } else {
                        sendError(400, response);
                    }
                }
        );
    }
}
