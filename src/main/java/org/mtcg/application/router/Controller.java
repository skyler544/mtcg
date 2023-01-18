package org.mtcg.application.router;

import org.mtcg.application.util.Pair;

import java.util.List;

public interface Controller {
    List<Pair<RouteIdentifier, Route>> listRoutes();
}
