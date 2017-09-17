package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class RouteStop {
    public ArrayList<Order> Orders = new ArrayList<>();
    public DateTime ArrivalTime;
    public Location Location;
    private int routeStopId;
    public RouteStop() {
    }

    public RouteStop(int routeStopId, DateTime arrivalTime, Location location, ArrayList<Order> orders) {
        this.routeStopId = routeStopId;
        this.ArrivalTime = arrivalTime;
        this.Location = location;
        this.Orders = orders;
    }

    public int getRouteStopId() {
        return routeStopId;
    }

    public void setRouteStopId(int routeStopId) {
        this.routeStopId = routeStopId;
    }
}
