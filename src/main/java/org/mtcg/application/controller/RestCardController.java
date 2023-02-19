package org.mtcg.application.controller;

import org.mtcg.application.router.Controller;
import org.mtcg.application.router.Route;
import org.mtcg.application.router.RouteIdentifier;
import org.mtcg.application.service.CardService;
import org.mtcg.application.service.UserService;
import org.mtcg.application.util.Pair;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;
import org.mtcg.http.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestCardController implements Controller {

    private final CardService cardService;
    private final UserService userService;

    public RestCardController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    public Response addPackage(RequestContext requestContext) {
        if (userService.adminAuthenticate(requestContext.getToken())) {
            cardService.addPackage(requestContext.getBody());
        } else {
            throw new UnauthorizedException("Authentication failure.");
        }
        Response response = new Response();
        response.setHttpStatus(HttpStatus.CREATED);
        return response;
    }

    public Response acquirePackage(RequestContext requestContext) {
        // TODO: implement me
        return new Response();
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> cardRoutes = new ArrayList<>();

        cardRoutes.add(new Pair<>(
                routeIdentifier("/packages", "POST"),
                this::addPackage));

        cardRoutes.add(new Pair<>(
                routeIdentifier("/transactions/packages", "POST"),
                this::acquirePackage));

        return cardRoutes;
    }
}
