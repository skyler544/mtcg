package org.mtcg.application.controller;

import org.mtcg.application.router.Controller;
import org.mtcg.application.router.Route;
import org.mtcg.application.router.RouteIdentifier;
import org.mtcg.application.service.BattleService;
import org.mtcg.application.service.UserService;
import org.mtcg.application.util.Pair;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestBattleController implements Controller {

    private final BattleService battleService;
    private final UserService userService;

    public RestBattleController(BattleService battleService, UserService userService) {
        this.battleService = battleService;
        this.userService = userService;
    }

    public Response getStats(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setBody(battleService.getStats(token));
        return response;
    }

    public Response getScoreboard(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setBody(battleService.getScoreboard());
        return response;
    }

    public Response battle(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setBody(battleService.battle(token));
        return response;
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> battleRoutes = new ArrayList<>();

        battleRoutes.add(new Pair<>(
                routeIdentifier("/stats", "GET"),
                this::getStats));

        battleRoutes.add(new Pair<>(
                routeIdentifier("/score", "GET"),
                this::getScoreboard));

        battleRoutes.add(new Pair<>(
                routeIdentifier("/battles", "POST"),
                this::battle));

        return battleRoutes;
    }
}
