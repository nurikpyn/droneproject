package de.reekind.droneproject;

import de.reekind.droneproject.model.RouteCalculator;
import de.reekind.droneproject.model.routeplanning.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by timbe on 12.08.2017.
 */
public class ProblemSolverHelper {
    final static Logger _log = LogManager.getLogger(ProblemSolverHelper.class);

    public static void main(String[] args) {

        RouteCalculator calculator = new RouteCalculator();
        ArrayList<Route> bestSolution = calculator.calculateRoute();

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(bestSolution));
        } catch (IOException ex) {
            _log.error("Fehler beim Mappen der Objekte in JSON", ex);
        }
            }
}
