package de.reekind.droneproject;

import de.reekind.droneproject.domain.RouteCalculator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by timbe on 12.08.2017.
 */
public class ProblemSolverHelper {
    // Define a static logger variable so that it references the
    // Logger instance named "MyApp".
    private static final Logger logger = LogManager.getLogger(ProblemSolverHelper.class);

    public static void main(String[] args) {
        RouteCalculator calculator = new RouteCalculator();
        logger.error("Test");
        calculator.calculateRoute();

    }
}
