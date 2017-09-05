package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Order;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RouteStop {
    public ArrayList<Order> Orders = new ArrayList<>();
    public double ArrivalTime;
}
