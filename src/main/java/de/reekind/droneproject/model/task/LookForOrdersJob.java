package de.reekind.droneproject.model.task;

import de.reekind.droneproject.model.routeplanning.RouteCalculator;

public class LookForOrdersJob implements Runnable{
    @Override
    public void run() {
        RouteCalculator calculator = new RouteCalculator();
        calculator.calculateRoute();
        System.out.println("Job läuft");
    }
}
