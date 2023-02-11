package org.mtcg.application.controller;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.User;
import org.mtcg.application.router.Controller;
import org.mtcg.application.router.Route;
import org.mtcg.application.router.RouteIdentifier;
import org.mtcg.application.service.UserService;
import org.mtcg.application.util.Pair;
import org.mtcg.http.BadRequestException;
import org.mtcg.http.HttpStatus;
import org.mtcg.http.RequestContext;
import org.mtcg.http.Response;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestUserController implements Controller {

    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    public Response register(RequestContext requestContext) {
        System.out.println("Body of register: " + requestContext.getBody());
        Credentials credentials = requestContext.getBodyAs(Credentials.class);
        return register(credentials);
    }

    public Response register(Credentials credentials) {
        String token = userService.findUserByUsername(credentials.getUsername());
        if (token != null) {
            throw new BadRequestException("User with username " + credentials.getUsername() + " already exists");
        } else {
            userService.persist(credentials);
        }
        Response response = new Response();
        response.setHttpStatus(HttpStatus.CREATED);
        return response;
    }

    public Response login(RequestContext requestContext) {
        Credentials credentials = requestContext.getBodyAs(Credentials.class);
        return login(credentials);
    }

    public Response login(Credentials credentials) {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> userRoutes = new ArrayList<>();

        userRoutes.add(new Pair<>(
                routeIdentifier("/users", "POST"),
                this::register
        ));

        userRoutes.add(new Pair<>(
            routeIdentifier("/sessions", "POST"),
            this::login
        ));


        return userRoutes;
    }
}
