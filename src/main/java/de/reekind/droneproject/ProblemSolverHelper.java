package de.reekind.droneproject;

import de.reekind.droneproject.model.routeplanning.RouteCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by timbe on 12.08.2017.
 */
public class ProblemSolverHelper {
    private final static Logger _log = LogManager.getLogger(ProblemSolverHelper.class);

    public static void main(String[] args) {

        RouteCalculator calculator = new RouteCalculator();
        calculator.calculateRoute();
        calculator.startDrones();
    }
}
