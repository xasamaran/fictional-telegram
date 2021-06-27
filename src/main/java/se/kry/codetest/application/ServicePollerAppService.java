package se.kry.codetest.application;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import se.kry.codetest.infrastructure.repository.ServiceRepository;
import se.kry.codetest.models.service.Service;

import java.util.List;

public class ServicePollerAppService {

    private final WebClient webClient;
    private ServiceRepository serviceRepository;

    public ServicePollerAppService(ServiceRepository serviceRepository, WebClient webClient) {
        this.serviceRepository = serviceRepository;
        this.webClient = webClient;
    }

    public Future<List<String>> pollServices() {
        this.serviceRepository.getAllService().setHandler(result -> {
            if (result.succeeded() && !result.result().isEmpty()) {
                result.result().forEach(this::pollService);
            }
        });

        return Future.failedFuture("TODO");
    }

    private void pollService(Service service) {
        System.out.println(service.getName()+" polling");
        this.webClient
                .head(443, service.getUrl(), "")
                .ssl(true)
                .send(httpResponseAsyncResult -> handlePoll(service, httpResponseAsyncResult));

    }

    private void handlePoll(Service service, AsyncResult<HttpResponse<Buffer>> httpResponseAsyncResult) {
            service.setAlive(httpResponseAsyncResult.succeeded());
            this.serviceRepository.patchServiceAlive(service);
    }

}
