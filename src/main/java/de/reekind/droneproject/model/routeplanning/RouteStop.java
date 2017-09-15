package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;

import java.util.ArrayList;

public class RouteStop {
    public void setRouteStopId(int routeStopId) {
        this.routeStopId = routeStopId;
    }

    private int routeStopId;
    public ArrayList<Order> Orders = new ArrayList<>();
    public double ArrivalTime;
    public Location Location;

    public RouteStop() {}

    public RouteStop(int routeStopId, double arrivalTime, Location location) {
        this.routeStopId = routeStopId;
        this.ArrivalTime = arrivalTime;
        this.Location = location;
    }

    public int getRouteStopId() {
        return routeStopId;
    }
}
