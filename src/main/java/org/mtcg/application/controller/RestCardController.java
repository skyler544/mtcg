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
import org.mtcg.http.exception.BadRequestException;
import org.mtcg.http.exception.ForbiddenException;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestCardController implements Controller {

    private final CardService cardService;
    private final UserService userService;
    private final int PACKAGE_PRICE = 5;

    public RestCardController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    public Response addPackage(RequestContext requestContext) {
        userService.adminAuthenticate(requestContext.getToken());
        cardService.addPackage(requestContext.getBody());
        Response response = new Response();
        response.setHttpStatus(HttpStatus.CREATED);
        return response;
    }

    public Response acquirePackage(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);
        if (userService.checkBalance(token) >= PACKAGE_PRICE) {
            // will throw a NotFoundException if no cards are available
            cardService.buyPackage(token);
            userService.subtractUserCoins(token, PACKAGE_PRICE);
        } else {
            throw new ForbiddenException("Not enough coins.");
        }
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    public Response getCards(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        String cards = cardService.returnUserCards(token);

        Response response = new Response();
        response.setHttpStatus(cards.equals("[]") ? HttpStatus.NO_CONTENT : HttpStatus.OK);
        response.setBody(cards);
        return response;
    }

    public Response setDeck(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);
        String[] cards = cardService.cardIdArray(requestContext.getBody());

        if (cards.length != 4) {
            throw new BadRequestException("The deck must consist of 4 cards.");
        }

        for (var id : cards) {
            cardService.doesUserOwnCard(id, token);
        }

        cardService.clearUserDeck(token);

        for (var id : cards) {
            cardService.addCardToDeck(id);
        }

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    public Response getDeck(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setBody(cardService.getUserDeck(token));
        return response;
    }

    public Response getDeckPlainText(RequestContext requestContext) {
        String token = requestContext.getToken();
        userService.authenticateToken(token);

        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setBody(cardService.getUserDeckPlainText(token));
        return response;
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

        cardRoutes.add(new Pair<>(
                routeIdentifier("/cards", "GET"),
                this::getCards));

        cardRoutes.add(new Pair<>(
                routeIdentifier("/deck", "PUT"),
                this::setDeck));

        cardRoutes.add(new Pair<>(
                routeIdentifier("/deck", "GET"),
                this::getDeck));

        cardRoutes.add(new Pair<>(
                routeIdentifier("/deck?format=plain", "GET"),
                this::getDeckPlainText));

        return cardRoutes;
    }
}
