package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class Route {
    public Integer RouteId;
    public Drone Drone;
    public DateTime StartTime;
    public DateTime EndTime;
    public ArrayList<RouteStop> RouteStops = new ArrayList<>();
    private RouteStatus routeStatus = RouteStatus.Geplant;
    public Location StartLocation;
    public double TotalDistance;

    public Route() {
    }

    public Route(int routeId, Drone drone, DateTime startTime, DateTime endTime, ArrayList<RouteStop> routeStops, RouteStatus routeStatus, Location startLocation, double totalDistance) {
        this.RouteId = routeId;
        this.Drone = drone;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.RouteStops = routeStops;
        this.routeStatus = routeStatus;
        this.StartLocation = startLocation;
        this.TotalDistance = totalDistance;
    }

    public RouteStatus getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(RouteStatus routeStatus) {
        this.routeStatus = routeStatus;
    }
}
