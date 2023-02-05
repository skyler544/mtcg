package org.mtcg;

import org.mtcg.application.controller.RestUserController;
import org.mtcg.application.repository.PostgresUserRepository;
import org.mtcg.application.router.Router;
import org.mtcg.application.service.UserService;

import org.mtcg.http.HttpServer;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService(new PostgresUserRepository());
        RestUserController restUserController = new RestUserController(userService);

        Router router = new Router();

        restUserController.listRoutes()
                .forEach(router::registerRoute);

        HttpServer httpServer = new HttpServer(router);
        httpServer.start();
    }
}
