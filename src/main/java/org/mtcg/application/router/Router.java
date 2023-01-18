package org.mtcg.application.router;

import org.mtcg.application.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private Map<RouteIdentifier, Route> routes = new HashMap<>();

    // Using the pair record, access to the individual elements of the map is
    // simple.
    public void registerRoute(Pair<RouteIdentifier, Route> routeIdentifierRoutePair) {
        routes.put(routeIdentifierRoutePair.left(), routeIdentifierRoutePair.right());
    }

    public Route findRoute(RouteIdentifier routeIdentifier) {
        return routes.get(routeIdentifier);
    }

}
