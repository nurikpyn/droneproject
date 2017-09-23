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

    public Route() {
    }

    public Route(int routeId, Drone drone, DateTime startTime, DateTime endTime, ArrayList<RouteStop> routeStops, RouteStatus routeStatus) {
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

    // Get the total distance from beginning to end of the route
    public static double getTotalRouteDistance(Route route)
    {
        System.out.println("NewRouteDist!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        double totalDistance = 0;
        RouteStop prevRS = null;
        for (RouteStop rs: route.RouteStops)
        {
            if (prevRS != null)
            {
                totalDistance += Location.distanceInKm(rs.Location.getLatitude(), rs.Location.getLongitude(),
                                                        prevRS.Location.getLatitude(), prevRS.Location.getLongitude());
                System.out.println("Route Distance = " + totalDistance);
            }
            prevRS = rs;
        }
        return totalDistance;
    }
}
