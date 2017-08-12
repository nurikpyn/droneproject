package de.reekind.droneproject.domain;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.sql.*;
import java.util.*;

// Hauptklasse für Verarbeitung
public class RouteCalculator {

    public List<Order> listOfOrders = new ArrayList<Order>();
    public List<Drone> listOfDrones = new ArrayList<Drone>();
    public List<DroneType> listOfDroneTypes = new ArrayList<DroneType>();

    public List<Service> listOfServices = new ArrayList<Service>();
    public List<VehicleType> listOfVehicleTypes = new ArrayList<VehicleType>();
    public List<Vehicle> listOfVehicles = new ArrayList<Vehicle>();

    public void calculateRoute()
    {
        // Get orders from DB
        getOrdersFromDB();
        // Get drones from DB
        // Also get dronetypes
        getDronesAndTypesFromDB();
        // Define VehicleTypes and Vehicles
        createVRPVehicleTypes();
        createVRPVehicles();
        // Add services to problem
        createVRPServices();
        // Solve problem
        solveVRPProblem();
    }

    private void getOrdersFromDB()
    {
        // TODO: configure connection
        Connection conn;


        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                                                    "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt;
            ResultSet rs;

            //Get Orders
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT orderId, orderTime, adressID, weight, status, droneId from orders ORDER BY orderId ASC");

            while (rs.next()) {
                Order order = new Order(rs.getInt("orderId"),
                                        rs.getDate("orderTime"),
                                        rs.getInt("adressID"),
                                        rs.getFloat("weight"),
                                        rs.getInt("status"));
                listOfOrders.add(order);
            }
            // Now we have all orders in our data structure

            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    private void getDronesAndTypesFromDB() {
        // TODO: configure connection
        Connection conn;

        try
        {
            conn =  DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt;
            ResultSet rs;

            //Get Orders
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT droneId, droneTypeId, status from drones");

            while (rs.next()) {
                // TODO: Set relative for droneTypes?
                Drone drone = new Drone(rs.getInt("droneId"),
                                        rs.getInt("droneTypeId"),
                                        rs.getInt("status"));
                listOfDrones.add(drone);
            }
            // Now we have all drones in our data structure

            //Get dronetypes
            // Join to only get the relevant types
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT dronetypes.droneTypeId, maxWeight, maxPackageCount, dronetypes.maxRange from dronetypes " +
                                        "LEFT JOIN drones ON dronetypes.droneTypeId = drones.droneTypeId " +
                                        "WHERE drones.droneTypeID IS NOT NULL " +
                                        "GROUP BY droneTypeId");
            while (rs.next()) {
                // TODO: Set relative for droneTypes
                DroneType droneType = new DroneType(rs.getInt("droneTypeId"),
                        rs.getFloat("maxWeight"),
                        rs.getInt("maxPackageCount"),
                        rs.getFloat("maxRange"));
                listOfDroneTypes.add(droneType);
            }

            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    private void createVRPVehicleTypes()
    {
        // TODO: create vehicleTypes here, edit dimensions
        int WEIGHT_INDEX = 0;
        int RANGE_INDEX = 1;
        for (DroneType dt: listOfDroneTypes)
        {
            VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance(Integer.toString(dt.getDroneTypeId()));
            vehicleTypeBuilder.addCapacityDimension(WEIGHT_INDEX, (int) dt.getMaxWeight());
            vehicleTypeBuilder.addCapacityDimension(RANGE_INDEX, (int) dt.getMaxRange());
            VehicleType vehicleType = vehicleTypeBuilder.build();
            listOfVehicleTypes.add(vehicleType);
        }
    }

    private void createVRPVehicles()
    {
        // TODO: fix Location and Type
        for (Drone drone: listOfDrones)
        {
            VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance(Integer.toString(drone.getDroneId()));
            vehicleBuilder.setStartLocation(Location.newInstance(10, 10));
            //vehicleBuilder.setType();
            // Only for test, returns wrong type!!!!
            vehicleBuilder.setType(listOfVehicleTypes.get(0));
            VehicleImpl vehicle = vehicleBuilder.build();
            listOfVehicles.add(vehicle);
        }
    }

    private void createVRPServices()
    {
        for (Order o: listOfOrders)
        {
            // TODO: Get builder working
            double locX = o.getAddressId();
            Service service1 = Service.Builder.newInstance(Integer.toString(o.getOrderId())).addSizeDimension(0, 1).setLocation(Location.newInstance(5, 7)).build();
            listOfServices.add(service1);
        }
    }



    private void solveVRPProblem()
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
        VehicleRoutingProblem problem = vrpBuilder.build();

        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        // Debug
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
    }


}
