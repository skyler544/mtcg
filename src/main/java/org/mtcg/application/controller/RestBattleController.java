package org.mtcg.application.controller;

import org.mtcg.application.router.Controller;
import org.mtcg.application.router.Route;
import org.mtcg.application.router.RouteIdentifier;
import org.mtcg.application.service.BattleService;
import org.mtcg.application.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.mtcg.application.router.RouteIdentifier.routeIdentifier;

public class RestBattleController implements Controller {

    private final BattleService battleService;

    public RestBattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public List<Pair<RouteIdentifier, Route>> listRoutes() {
        List<Pair<RouteIdentifier, Route>> battleRoutes = new ArrayList<>();

        return battleRoutes;
    }
}
