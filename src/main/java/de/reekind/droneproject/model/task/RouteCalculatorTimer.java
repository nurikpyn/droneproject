package de.reekind.droneproject.model.task;

import de.reekind.droneproject.model.routeplanning.RouteCalculator;

import java.util.TimerTask;

public class RouteCalculatorTimer extends TimerTask {
    @Override
    public void run() {
        RouteCalculator calculator = new RouteCalculator();
        calculator.calculateRoute();
    }
}
