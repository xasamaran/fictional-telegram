package se.kry.codetest.infrastructure.controller;

import io.vertx.core.http.HttpServerResponse;

public interface Controller {

    default void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusMessage(response.getStatusMessage()).setStatusCode(statusCode).end();
    }
}
