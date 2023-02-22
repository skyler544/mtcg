package org.mtcg;

import org.mtcg.application.controller.RestUserController;
import org.mtcg.application.controller.RestBattleController;
import org.mtcg.application.controller.RestCardController;
import org.mtcg.application.controller.RestTradeController;
import org.mtcg.application.repository.PostgresUserRepository;
import org.mtcg.application.router.Router;
import org.mtcg.application.service.BattleService;
import org.mtcg.application.service.CardService;
import org.mtcg.application.service.TradeService;
import org.mtcg.application.repository.PostgresBattleRepository;
import org.mtcg.application.repository.PostgresCardRepository;
import org.mtcg.application.repository.PostgresTradeRepository;
import org.mtcg.application.service.UserService;

import org.mtcg.http.HttpServer;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService(new PostgresUserRepository());
        RestUserController restUserController = new RestUserController(userService);

        CardService cardService = new CardService(new PostgresCardRepository());
        RestCardController restCardController = new RestCardController(userService, cardService);

        TradeService tradeService = new TradeService(new PostgresTradeRepository(), new PostgresCardRepository());
        RestTradeController restTradeController = new RestTradeController(tradeService, userService);

        BattleService battleService = new BattleService(new PostgresBattleRepository(), new PostgresUserRepository(), new PostgresCardRepository());
        RestBattleController restBattleController = new RestBattleController(battleService, userService);

        Router router = new Router();

        restUserController.listRoutes()
                .forEach(router::registerRoute);

        restCardController.listRoutes()
                .forEach(router::registerRoute);

        restTradeController.listRoutes()
                .forEach(router::registerRoute);

        restBattleController.listRoutes()
                .forEach(router::registerRoute);

        HttpServer httpServer = new HttpServer(router);
        httpServer.start();
    }
}
