package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class RouteStop {
    public Order Order;
    public DateTime ArrivalTime;
    public Location Location;
    private int routeStopId;
    public double RouteDistanceTillStop;
    public RouteStop() {
    }

    public RouteStop(int routeStopId, DateTime arrivalTime, Location location, Order order, double routeDistanceTillStop) {
        this.routeStopId = routeStopId;
        this.ArrivalTime = arrivalTime;
        this.Location = location;
        this.Order = order;
        this.RouteDistanceTillStop = routeDistanceTillStop;
    }

    public int getRouteStopId() {
        return routeStopId;
    }

    public void setRouteStopId(int routeStopId) {
        this.routeStopId = routeStopId;
    }
}
