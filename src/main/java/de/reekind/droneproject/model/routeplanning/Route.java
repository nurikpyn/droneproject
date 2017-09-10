package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Drone;

import java.util.ArrayList;

public class Route {
    public Drone Drone;
    public double StartTime;
    public double EndTime;
    public ArrayList<RouteStop> RouteStops = new ArrayList<>();
}
