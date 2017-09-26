package de.reekind.droneproject.model.routeplanning;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.util.Solutions;
import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.dao.DroneDAO;
import de.reekind.droneproject.dao.LocationDAO;
import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.dao.RouteDAO;
import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import de.reekind.droneproject.model.enumeration.OrderStatus;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.task.DroneTimer;
import de.reekind.droneproject.model.task.OrderTimer;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;
import de.reekind.droneproject.model.task.RouteTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;

// Hauptklasse für Verarbeitung
public class RouteCalculator {

    private static Logger _log = LogManager.getLogger();
    private static Timer timer = new Timer();

    private ArrayList<Service> listOfServices = new ArrayList<>();
    private ArrayList<Vehicle> listOfVehicles = new ArrayList<>();

    public RouteCalculator() {
        DbUtil.getConnection();
    }

    public ArrayList<Route> calculateRoute() {
        _log.debug("Berechnen der optimalen Routen für alle verfügbaren Drohnen und Bestellungen");
        // Define VehicleTypes and Vehicles
        // For each drone, create a corresponding Vehicle in Jsprit
        for (Drone drone : DroneDAO.getDronesWithStatus(DroneStatus.Bereit)) {
            listOfVehicles.add(drone.toJspritVehicle());
        }
        if (_log.isDebugEnabled()) _log.debug("Anzahl Drohnen, die verfügbar sind: {}", listOfVehicles.size());
        if (listOfVehicles.size() == 0)
            return null;
        // Add services to problem
        for (Order o : OrderDAO.getOrdersWithStatus(OrderStatus.Bereit)) {
            listOfServices.add(o.toJspritService());
        }
        if (_log.isDebugEnabled()) _log.debug("Anzahl Bestellungen, die verfügbar sind: {}", listOfServices.size());
        if (listOfServices.size() == 0)
            return null;

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
        //SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        // Writing back the solution in Routes
        ArrayList<Route> routes = new ArrayList<>();

        // For each route in in the best solution...
        for (VehicleRoute jspritRoute : bestSolution.getRoutes()) {
            //Tools for counting distance
            double totalDistance = 0;
            double distanceToStop;
            RouteStop prevRS = null;
            int counter = 0;

            //Anlegen der Route im Objektmodell
            Route route = new Route();
            route.Drone = DroneDAO.getDrone(Integer.parseInt(jspritRoute.getVehicle().getId()));
            //route.StartTime = Timestamp.from(Instant.ofEpochMilli((long)jspritRoute.getDepartureTime()));
            //route.EndTime =  Timestamp.from(Instant.ofEpochMilli((long)jspritRoute.getEnd().getEndTime()));
            com.graphhopper.jsprit.core.problem.Location startLocation = jspritRoute.getStart().getLocation();
            route.StartLocation = new Location(startLocation.getCoordinate().getX(), startLocation.getCoordinate().getY());
            route.StartTime = DateTime.now();
            route.EndTime = DateTime.now();

            // Anlegen der Routenstopps im Objektmodell
            for (TourActivity activity : jspritRoute.getActivities()) {
                //Laden der JobId - OrderId in der Bestelltabelle
                String jobId = "-1";
                if (activity instanceof TourActivity.JobActivity) {
                    jobId = ((TourActivity.JobActivity) activity).getJob().getId();
                }
                RouteStop stop = new RouteStop();
                Order order = OrderDAO.getOrder(Integer.parseInt(jobId));
                order.setOrderStatus(OrderStatus.Geplant);
                OrderDAO.updateOrder(order);
                stop.Order = order;
                stop.Location = LocationDAO.getLocation(activity.getLocation().getCoordinate());

                //Berechnen der Distanz
                //Erster Stop --> Depot zur ersten Location
                if (counter == 0) {
                    totalDistance += Location.distanceInKm(stop.Location.getLatitude(), stop.Location.getLongitude(),
                            route.StartLocation.getLatitude(), route.StartLocation.getLongitude());
                    distanceToStop = totalDistance;
                    //Normaler Stop --> Vorherige Location zur jetzigen
                } else {
                    totalDistance += Location.distanceInKm(stop.Location.getLatitude(), stop.Location.getLongitude(),
                            prevRS.Location.getLatitude(), prevRS.Location.getLongitude());
                    distanceToStop = totalDistance;
                }
                //Letzter Stop --> Adde Location zu Depot
                if (counter == jspritRoute.getActivities().size() - 1)
                    totalDistance += Location.distanceInKm(stop.Location.getLatitude(), stop.Location.getLongitude(),
                            route.StartLocation.getLatitude(), route.StartLocation.getLongitude());
                prevRS = stop;
                counter++;
                stop.RouteDistanceTillStop = distanceToStop;

                //Hinzufügen des Stops zu den Routenstops der Route im Modell
                route.RouteStops.add(stop);
            }
            //Gesamte Distanz der Route
            route.TotalDistance = totalDistance;
            route = RouteDAO.addRoute(route);
            routes.add(route);
        }
        return routes;
    }

    public void startDrones() {
        //Starte alle Drohnen für die die Route geplant wurde
        for (Route route : RouteDAO.getAllRoutesWithStatus(RouteStatus.Geplant)) {


            //Wir gehen davon aus, dass zu diesem Zeitpunkt die Drohne Bereit ist
            if (route.Drone.getDroneStatus() != DroneStatus.Bereit) {
                return;
            }
            route.Drone.setDroneStatus(DroneStatus.InAuslieferung);
            DroneDAO.updateDrone(route.Drone);
            route.setRouteStatus(RouteStatus.InAuslieferung);
            for (RouteStop routeStop : route.RouteStops) {
                //Die Bestellungen müssen ebenfalls bereit sein.
                if (routeStop.Order.getOrderStatus() != OrderStatus.Geplant)
                    return;

                routeStop.Order.setOrderStatus(OrderStatus.InAuslieferung);
                // Timer, der sobald die Bestellung ausgelierfert ist diese auf ausgeliefert stellt
                double durationInHours = routeStop.RouteDistanceTillStop / route.Drone.getDroneType().getMaxSpeed();
                long timerDurationInMillis = Math.round(DateTimeConstants.MILLIS_PER_HOUR * durationInHours);
                routeStop.Order.setDeliveryTime(DateTime.now().plusMinutes((int) (durationInHours / 60)));
                OrderTimer orderDeliveredTimer = new OrderTimer(routeStop.Order, OrderStatus.Ausgeliefert);
                timer.schedule(orderDeliveredTimer, timerDurationInMillis);

                routeStop.ArrivalTime = DateTime.now().plusMillis((int) timerDurationInMillis);

                _log.debug("Started timer for order " + routeStop.Order.getOrderId() + " duration: " + timerDurationInMillis / 1000 + " seconds.");
                OrderDAO.updateOrder(routeStop.Order);
            }
            RouteDAO.updateRoute(route);

            // Get duration in hours, then schedule task execution ahead from now.
            //double durationInHours = Route.getTotalRouteDistance(route)/route.Drone.getDroneType().getMaxSpeed();
            double durationInHours = route.TotalDistance / route.Drone.getDroneType().getMaxSpeed();
            long timerDurationInMillis = Math.round(DateTimeConstants.MILLIS_PER_HOUR * durationInHours);
            DroneTimer droneTimer = new DroneTimer(route.Drone, DroneStatus.Bereit);
            timer.schedule(droneTimer, timerDurationInMillis);
            // Debug
            _log.debug("Started timer for drone " + route.Drone.getDroneId() + " duration: " + timerDurationInMillis / 1000 + " seconds.");

            RouteTimer routeTimer = new RouteTimer(route, RouteStatus.Beendet);
            timer.schedule(routeTimer, timerDurationInMillis);
            // Debug
            _log.debug("Started timer for route " + route.RouteId + " duration: " + timerDurationInMillis / 1000 + " seconds.");
        }
    }
}
