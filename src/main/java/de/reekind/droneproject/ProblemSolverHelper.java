package de.reekind.droneproject;

import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import de.reekind.droneproject.dao.DroneDAO;
import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.RouteCalculator;
import de.reekind.droneproject.model.routeplanning.DeliveryPlan;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by timbe on 12.08.2017.
 */
public class ProblemSolverHelper {

    public static void main(String[] args) {
        RouteCalculator calculator = new RouteCalculator();
        VehicleRoutingProblemSolution bestSolution = calculator.calculateRoute();
        DeliveryPlan plan = new DeliveryPlan();
        // For each route in in the best solution...
        for (VehicleRoute jspritRoute : bestSolution.getRoutes()) {
            Route route = new Route();
            route.Drone = DroneDAO.getDrone(Integer.parseInt(jspritRoute.getVehicle().getId()));
            route.StartTime = jspritRoute.getDepartureTime();
            route.EndTime = jspritRoute.getEnd().getEndTime();
            // for each point in the route...
            for (TourActivity activity : jspritRoute.getActivities()) {
                String jobId = "-1";
                if (activity instanceof TourActivity.JobActivity) {
                    jobId = ((TourActivity.JobActivity)activity).getJob().getId();
                }
                RouteStop stop = new RouteStop();
                stop.Orders.add(OrderDAO.getOrder(Integer.parseInt(jobId)));
                stop.ArrivalTime = activity.getArrTime();
                route.RouteStops.add(stop);
            }
            plan.Routes.add(route);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(plan));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
