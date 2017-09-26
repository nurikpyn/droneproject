package de.reekind.droneproject.model.task;

import de.reekind.droneproject.dao.RouteDAO;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;

import java.util.TimerTask;

public class RouteTimer extends TimerTask {
    private Route route;
    private RouteStatus routeStatus;

    public RouteTimer(Route route, RouteStatus statusToSet) {
        this.route = route;
        this.routeStatus = statusToSet;
    }

    @Override
    public void run() {
        this.route.setRouteStatus(routeStatus);
        RouteDAO.updateRoute(route);
    }
}
