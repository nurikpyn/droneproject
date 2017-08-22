package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Hauptklasse für Verarbeitung
public class RouteCalculator {
    private static Connection conn;

    public ArrayList<Order> listOfOrders = new ArrayList<Order>();
    public List<Drone> listOfDrones = new ArrayList<Drone>();
    public List<DroneType> listOfDroneTypes = new ArrayList<DroneType>();
    public ArrayList<Depot> listOfDepots = new ArrayList<Depot>();

    public List<Service> listOfServices = new ArrayList<Service>();
    public List<Vehicle> listOfVehicles = new ArrayList<Vehicle>();

    public VehicleRoutingProblemSolution calculateRoute()
    {
        // Get orders from DB
        getOrdersFromDB();
        // Get drones from DB
        // Also get dronetypes
        getDronesAndTypesFromDB();
        // Define VehicleTypes and Vehicles
        createVRPVehicles();
        // Add services to problem
        createVRPServices();
        // Solve problem
       return  solveVRPProblem();
    }

    private void getOrdersFromDB()
    {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt;
            ResultSet rs;

            //Get Orders
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT orderId, orderTime, adresses.latitude, adresses.longitude, weightInGrams, orderStatus, droneId " +
                                        "FROM orders " +
                                        "JOIN adresses on adresses.adressID = orders.adressID " +
                                        "ORDER BY orderId ASC");

            while (rs.next()) {
                Order order = new Order(rs.getInt("orderId"),
                                        rs.getTimestamp("orderTime"),
                                        rs.getDouble("latitude"),
                                        rs.getDouble("longitude"),
                                        rs.getInt("weightInGrams"),
                                        rs.getInt("orderStatus"));
                listOfOrders.add(order);
            }
            // Now we have all orders in our data structure

            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void getDronesAndTypesFromDB() {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt;
            ResultSet rs;
            //Get dronetypes
            // Join to only get the relevant types
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT dronetypes.droneTypeId, maxWeightInGrams, maxPackageCount, dronetypes.maxRange " +
                                        "FROM dronetypes " +
                                        "LEFT JOIN drones ON dronetypes.droneTypeId = drones.droneTypeId " +
                                        "WHERE drones.droneTypeID IS NOT NULL " +
                                        "GROUP BY droneTypeId");
            while (rs.next()) {
                DroneType droneType = new DroneType(rs.getInt("droneTypeId"),
                        rs.getFloat("maxWeightInGrams"),
                        rs.getInt("maxPackageCount"),
                        rs.getFloat("maxRange"));
                listOfDroneTypes.add(droneType);
            }
            //Get Depots
            // Join to only get the relevant types
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT depots.depotID, depots.latitude, depots.longitude " +
                                        "FROM depots " +
                                        "LEFT JOIN drones ON depots.depotID = drones.droneDepotID " +
                                        "WHERE drones.droneDepotID IS NOT NULL " +
                                        "GROUP BY drones.droneDepotID");
            while (rs.next()) {
                Depot depot = new Depot(rs.getInt("depotID"),
                        new de.reekind.droneproject.model.Location(rs.getFloat("latitude"),
                                rs.getInt("longitude"))
                       );
                listOfDepots.add(depot);
            }
            //Get Drones
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT droneId, droneTypeID, droneStatus, droneDepotID FROM drones");

            while (rs.next()) {
                int droneTypeIndex = listOfDroneTypes.indexOf(new DroneType(rs.getInt("droneTypeID")));
                int depotIndex = listOfDepots.indexOf(new Depot(rs.getInt("droneDepotID")));
                DroneType droneType = null;
                if (droneTypeIndex > -1) {
                    droneType = listOfDroneTypes.get(droneTypeIndex);
                }
                Depot depot = null;
                if (depotIndex > -1) {
                    depot = listOfDepots.get(depotIndex);
                }
                Drone drone = new Drone(rs.getInt("droneId"),
                        droneType,
                                        rs.getInt("droneStatus"),depot);
                listOfDrones.add(drone);
            }
            // Now we have all drones in our data structure

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    private void createVRPVehicles()
    {
        // For each drone, create a corresponding Vehicle in Jsprit
        for (Drone drone: listOfDrones)
        {
            VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance(Integer.toString(drone.getDroneId()));
            vehicleBuilder.setStartLocation(drone.getDroneDepot().getLocation().toJspritLocation());
            vehicleBuilder.setType(drone.getDroneType().toJspritVehicleType());
            VehicleImpl vehicle = vehicleBuilder.build();
            listOfVehicles.add(vehicle);
        }
    }

    private void createVRPServices()
    {
        // For each order, create a corresponding Service in Jsprit
        for (Order o: listOfOrders)
        {
            Service serv;
            Service.Builder servBuilder = Service.Builder.newInstance(Integer.toString(o.getOrderId()));
            servBuilder.addSizeDimension(DroneType.WEIGHT_INDEX, (int) o.getWeight());
            servBuilder.setLocation(o.getLocation().toJspritLocation());
            serv = servBuilder.build();
            listOfServices.add(serv);
        }
    }

    private VehicleRoutingProblemSolution solveVRPProblem()
    {
        // TODO:
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        for (Vehicle v: listOfVehicles)
        {
            vrpBuilder.addVehicle(v);
        }
        for (Service s: listOfServices)
        {
            vrpBuilder.addJob(s);
        }
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        //TODO Set cost /speed
        VehicleRoutingProblem problem = vrpBuilder.build();

        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        // Debug
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        return bestSolution;
    }
}