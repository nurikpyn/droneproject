package de.reekind.droneproject.model.task;

import de.reekind.droneproject.model.routeplanning.RouteCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LookForOrdersJob implements Runnable{
    private final static Logger _log = LogManager.getLogger();
    @Override
    public void run() {
        _log.debug("15-minütiger Job Läuft");
        RouteCalculator calculator = new RouteCalculator();
        calculator.calculateRoute();
        calculator.startDrones();
        _log.debug("Berechnung der Route fertig.");
    }
}
