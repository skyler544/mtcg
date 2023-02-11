package org.mtcg.application.controller;

import org.mtcg.application.model.Credentials;
import org.mtcg.application.model.UserProfile;
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

    public Response updateProfile(RequestContext requestContext) {
        // data transformation:

        // get username from route
        String username = requestContext.getPath().split("/")[2];
        String token = "";

        for (var header : requestContext.getHeaders()) {
            // get token from header
            if (header.getName().equals("Authorization")) {
                // we don't need the word "Bearer"
                token = header.getValue().split(" ")[1];
            }
        }

        UserProfile userProfile = requestContext.getBodyAs(UserProfile.class);
        return updateProfile(token, username, userProfile);
    }

    public Response updateProfile(String token, String username, UserProfile userProfile) {
        userService.persistUserProfile(token, username, userProfile);

        // if anything went wrong an exception will have been thrown from one of
        // the underlying layers, so we must have been successful and can now
        // send an OK
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> userRoutes = new ArrayList<>();

        userRoutes.add(new Pair<>(
                routeIdentifier("/users", "POST"),
                this::register));

        userRoutes.add(new Pair<>(
                routeIdentifier("/sessions", "POST"),
                this::login));

        userRoutes.add(new Pair<>(
                routeIdentifier("/users/{username}", "PUT"),
                this::updateProfile));

        return userRoutes;
    }
}
