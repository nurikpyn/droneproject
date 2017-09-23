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

    public Route() {
    }

    public Route(int routeId, Drone drone, DateTime startTime, DateTime endTime, ArrayList<RouteStop> routeStops, RouteStatus routeStatu, Location startLocation) {
        this.RouteId = routeId;
        this.Drone = drone;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.RouteStops = routeStops;
        this.routeStatus = routeStatus;
        this.StartLocation = startLocation;
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
        int counter = 0;
        for (RouteStop rs: route.RouteStops)
        {
            //Erster Stop --> Depot zur ersten Location
            if (counter == 0){
                totalDistance += Location.distanceInKm(rs.Location.getLatitude(), rs.Location.getLongitude(),
                        route.StartLocation.getLatitude(),  route.StartLocation.getLongitude());
            //Normaler Stop --> Vorherige Location zur jetzigen
            } else {
                totalDistance += Location.distanceInKm(rs.Location.getLatitude(), rs.Location.getLongitude(),
                            prevRS.Location.getLatitude(), prevRS.Location.getLongitude());
            }
            //Letzter Stop --> Adde Location zu Depot
            if (counter == route.RouteStops.size() - 1)
                totalDistance += Location.distanceInKm(rs.Location.getLatitude(), rs.Location.getLongitude(),
                        route.StartLocation.getLatitude(),  route.StartLocation.getLongitude());
            prevRS = rs;
            counter++;

            System.out.println("Route Distance = " + totalDistance);
        }

        return totalDistance;
    }
}
