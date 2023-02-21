package org.mtcg.application.controller;

import org.mtcg.application.router.Controller;
import org.mtcg.application.router.Route;
import org.mtcg.application.router.RouteIdentifier;
import org.mtcg.application.service.TradeService;
import org.mtcg.application.service.UserService;
import org.mtcg.application.util.Pair;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestTradeController implements Controller {

    private final TradeService tradeService;
    private final UserService userService;

    public RestTradeController(TradeService tradeService, UserService userService) {
        this.tradeService = tradeService;
        this.userService = userService;
    }

    public Response postTrade(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        tradeService.postTrade(requestContext.getBody(), token);
        Response response = new Response();
        response.setHttpStatus(HttpStatus.CREATED);
        return response;
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> tradeRoutes = new ArrayList<>();

        tradeRoutes.add(new Pair<>(
                routeIdentifier("/tradings", "POST"),
                this::postTrade));

        return tradeRoutes;
    }
}
