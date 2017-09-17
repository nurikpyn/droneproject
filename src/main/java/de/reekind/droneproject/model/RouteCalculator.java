package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.dao.DroneDAO;
import de.reekind.droneproject.dao.OrderDAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

// Hauptklasse für Verarbeitung
public class RouteCalculator {

    private static Connection conn;

    /*public ArrayList<Order> listOfOrders = new ArrayList<>();
    public ArrayList<Drone> listOfDrones = new ArrayList<>();
    public ArrayList<DroneType> listOfDroneTypes = new ArrayList<>();
    public ArrayList<Depot> listOfDepots = new ArrayList<>();*/

    public ArrayList<Service> listOfServices = new ArrayList<>();
    public ArrayList<Vehicle> listOfVehicles = new ArrayList<>();

    public RouteCalculator() {
        DbUtil.getConnection();
    }

    public VehicleRoutingProblemSolution calculateRoute() {
        // Define VehicleTypes and Vehicles
        // For each drone, create a corresponding Vehicle in Jsprit
        for (Drone drone : DroneDAO.getAllDrones()) {
            listOfVehicles.add(drone.toJspritVehicle());
        }
        // Add services to problem
        for (Order o : OrderDAO.getAllOrders()) {
            listOfServices.add(o.toJspritService());
        }
        // Solve problem
        return solveVRPProblem();
    }


    private VehicleRoutingProblemSolution solveVRPProblem() {
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        for (Vehicle v : listOfVehicles) {
            vrpBuilder.addVehicle(v);
        }
        for (Service s : listOfServices) {
            vrpBuilder.addJob(s);
        }
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        //TODO Set cost /speed
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder;
        costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

        VehicleRoutingProblem problem = vrpBuilder.build();

        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        // Debug
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        return bestSolution;
    }
}
