package se.kry.codetest.infrastructure.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import se.kry.codetest.application.ModificationServiceAppService;
import se.kry.codetest.application.DeleteServiceAppService;
import se.kry.codetest.application.ServiceAppService;
import se.kry.codetest.application.ServicePollerAppService;
import se.kry.codetest.infrastructure.controller.ServiceController;
import se.kry.codetest.infrastructure.repository.DBConnector;
import se.kry.codetest.infrastructure.repository.ServiceRepository;
import se.kry.codetest.models.service.validators.ServiceValidator;

public class MainVerticle extends AbstractVerticle {

    private DBConnector connector;
    private Router router;
    private WebClient webClient;

    private ServicePollerAppService servicePollerAppService;

    private ServiceAppService serviceAppService;
    private ModificationServiceAppService modificationServiceAppService;
    private DeleteServiceAppService deleteServiceAppService;

    private ServiceRepository serviceRepository;

    private ServiceValidator serviceValidator = new ServiceValidator();

    private ServiceController serviceController;

    @Override
    public void start(Future<Void> startFuture) {
        this.connector = new DBConnector(vertx);
        this.router = Router.router(vertx);
        this.webClient = WebClient.create(vertx);
        this.router.route().handler(BodyHandler.create());

        this.serviceRepository = new ServiceRepository(connector);

        this.servicePollerAppService = new ServicePollerAppService(serviceRepository, webClient);
        this.serviceAppService = new ServiceAppService(serviceRepository);
        this.modificationServiceAppService = new ModificationServiceAppService(serviceRepository, serviceValidator);
        this.deleteServiceAppService = new DeleteServiceAppService(serviceRepository);

        this.serviceController = new ServiceController(router, serviceAppService, modificationServiceAppService, deleteServiceAppService);

        verticleStart(startFuture);
    }


    private void verticleStart(Future<Void> startFuture) {
        vertx.setPeriodic(1000 * 2, timerId -> {
            System.out.println("Polling Services");
            servicePollerAppService.pollServices();
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, result -> {
                            if (result.succeeded()) {
                                System.out.println("Service Controller Started");
                                startFuture.complete();
                            } else {
                                startFuture.fail(result.cause());
                            }
                        }
                );

    }

}




