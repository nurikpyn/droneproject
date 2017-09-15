package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.enumeration.RouteStatus;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Route {
    public Integer RouteId;
    public Drone Drone;
    private RouteStatus routeStatus = RouteStatus.Geplant;
    public double StartTime;
    public double EndTime;
    public ArrayList<RouteStop> RouteStops = new ArrayList<>();

    public Route() {}

    public Route(int routeId, Drone drone, double startTime, double endTime, ArrayList<RouteStop> routeStops, RouteStatus routeStatus) {
        this.RouteId = routeId;
        this.Drone = drone;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.RouteStops = routeStops;
        this.routeStatus = routeStatus;
    }

    public RouteStatus getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(RouteStatus routeStatus) {
        this.routeStatus = routeStatus;
    }
}
