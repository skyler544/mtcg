package org.mtcg;

import org.mtcg.application.router.Router;
import org.mtcg.http.HttpServer;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();
        HttpServer httpServer = new HttpServer(router);
        httpServer.start();
    }
}
