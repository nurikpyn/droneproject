package de.reekind.droneproject;

import de.reekind.droneproject.model.routeplanning.RouteCalculator;
import de.reekind.droneproject.model.routeplanning.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by timbe on 12.08.2017.
 */
public class ProblemSolverHelper {
    private final static Logger _log = LogManager.getLogger(ProblemSolverHelper.class);

    public static void main(String[] args) {

        RouteCalculator calculator = new RouteCalculator();
        ArrayList<Route> bestSolution = calculator.calculateRoute();
        //TODO geplante Routen werden hier sofort gestartet. Man sollte aber nur die Sofort starten, bei denen die Drohnen auch da sind
        calculator.startDrones();

        /*ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(bestSolution));
        } catch (IOException ex) {
            _log.error("Fehler beim Mappen der Objekte in JSON", ex);
        }*/
    }
}
