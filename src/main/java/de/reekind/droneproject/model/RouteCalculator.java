package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.dao.DroneDAO;
import de.reekind.droneproject.dao.LocationDAO;
import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.dao.RouteDAO;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import de.reekind.droneproject.model.enumeration.OrderStatus;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.DeliveryPlan;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

// Hauptklasse für Verarbeitung
public class RouteCalculator {

    private static Connection conn;

    public ArrayList<Service> listOfServices = new ArrayList<>();
    public ArrayList<Vehicle> listOfVehicles = new ArrayList<>();

    public RouteCalculator() {
        DbUtil.getConnection();
    }

    public ArrayList<Route> calculateRoute() {
        // Define VehicleTypes and Vehicles
        // For each drone, create a corresponding Vehicle in Jsprit
        for (Drone drone : DroneDAO.getAllDrones()) {
            listOfVehicles.add(drone.toJspritVehicle());
        }
        // Add services to problem
        for (Order o : OrderDAO.getAllOrders()) {
            listOfServices.add(o.toJspritService());
        }

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        for (Vehicle v : listOfVehicles) {
            vrpBuilder.addVehicle(v);
        }
        for (Service s : listOfServices) {
            vrpBuilder.addJob(s);
        }
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        VehicleRoutingProblem problem = vrpBuilder.build();

        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        // Debug
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        // Writing back the solution in Routes
        ArrayList<Route> routes = new ArrayList<>();

        // For each route in in the best solution...
        for (VehicleRoute jspritRoute : bestSolution.getRoutes()) {
            Route route = new Route();
            route.Drone = DroneDAO.getDrone(Integer.parseInt(jspritRoute.getVehicle().getId()));
            //route.StartTime = Timestamp.from(Instant.ofEpochMilli((long)jspritRoute.getDepartureTime()));
            //route.EndTime =  Timestamp.from(Instant.ofEpochMilli((long)jspritRoute.getEnd().getEndTime()));

            route.StartTime = DateTime.now();
            route.EndTime = DateTime.now();
            // for each point in the route...
            for (TourActivity activity : jspritRoute.getActivities()) {
                String jobId = "-1";
                if (activity instanceof TourActivity.JobActivity) {
                    jobId = ((TourActivity.JobActivity) activity).getJob().getId();
                }
                RouteStop stop = new RouteStop();
                stop.Orders.add(OrderDAO.getOrder(Integer.parseInt(jobId)));
                stop.Location = LocationDAO.getLocation(activity.getLocation().getCoordinate());
                //stop.ArrivalTime = activity.getArrTime();
                route.RouteStops.add(stop);
            }
            route = RouteDAO.addRoute(route);
            routes.add(route);
            startDrones();
        }
        return routes;
    }

    private void startDrones() {
       //Starte alle Drohnen für die die Route geplant wurde
        for (Route route : RouteDAO.getAllRoutesWithStatus(RouteStatus.Geplant)) {
            route.Drone.setDroneStatus(DroneStatus.InAuslieferung);
            DroneDAO.updateDrone(route.Drone);
            route.setRouteStatus(RouteStatus.InAuslieferung);
            for(RouteStop routeStop : route.RouteStops) {
                for(Order order : routeStop.Orders){
                    order.setOrderStatus(OrderStatus.InAuslieferung);
                }
            }
            RouteDAO.updateRoute(route);
            route.Drone.setReturnTimerFromDistance(Route.getTotalRouteDistance(route));
        }
    }
}
