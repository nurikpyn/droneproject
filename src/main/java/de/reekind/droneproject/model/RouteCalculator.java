package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.*;
import de.reekind.droneproject.DbUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Hauptklasse für Verarbeitung
@Path("/deliveryplan")
public class RouteCalculator {

    private static Connection conn;

    public ArrayList<Order> listOfOrders = new ArrayList<>();
    public List<Drone> listOfDrones = new ArrayList<>();
    public List<DroneType> listOfDroneTypes = new ArrayList<>();
    public ArrayList<Depot> listOfDepots = new ArrayList<>();

    public List<Service> listOfServices = new ArrayList<>();
    public List<Vehicle> listOfVehicles = new ArrayList<>();

    public RouteCalculator() {
        DbUtil.getConnection();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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

    /**
     * Lade Bestellungen aus der Datenbank (hinterher aus dem DAO, welche den Kriterien entsprechen
     * Status: Fertig zur Abfertigung
     * Zeit ..
     * TODO: Das einbauen
     */
    private void getOrdersFromDB()
    {
        try
        {
            conn =  DbUtil.getConnection();
            Statement stmt;
            ResultSet rs;

            //Get Orders
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT orderId, orderTime, adresses.latitude, adresses.longitude, weight, orderStatus, droneId " +
                                        "FROM orders " +
                                        "JOIN adresses on adresses.adressID = orders.adressID " +
                                        "ORDER BY orderId ASC");

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderId")
                        ,rs.getTimestamp("orderTime")
                        ,new Location(rs.getDouble("latitude")
                        ,rs.getDouble("longitude"))
                        ,rs.getInt("weight")
                        ,rs.getInt("orderStatus"));
                listOfOrders.add(order);
            }
            // Now we have all orders in our data structure

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
            conn =  DbUtil.getConnection();
            Statement stmt;
            ResultSet rs;
            //Get dronetypes
            // Join to only get the relevant types
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT dronetypes.droneTypeId, dronetypes.droneTypeName, maxWeightInGrams, maxPackageCount, dronetypes.maxRange " +
                                        "FROM dronetypes " +
                                        "LEFT JOIN drones ON dronetypes.droneTypeId = drones.droneTypeId " +
                                        "WHERE drones.droneTypeID IS NOT NULL " +
                                        "GROUP BY droneTypeId");
            while (rs.next()) {
                DroneType droneType = new DroneType(
                        rs.getInt("droneTypeId")
                        ,rs.getString("droneTypeName")
                        ,rs.getInt("maxWeightInGrams")
                        ,rs.getInt("maxPackageCount")
                        ,rs.getFloat("maxRange"));
                listOfDroneTypes.add(droneType);
            }
            //Get Depots
            // Join to only get the relevant types
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT depots.depotID, depots.name, depots.latitude, depots.longitude " +
                                        "FROM depots " +
                                        "LEFT JOIN drones ON depots.depotID = drones.droneDepotID " +
                                        "WHERE drones.droneDepotID IS NOT NULL " +
                                        "GROUP BY drones.droneDepotID");
            while (rs.next()) {
                Depot depot = new Depot(
                        rs.getInt("depotID")
                        , rs.getString("name")
                        ,new de.reekind.droneproject.model.Location(
                                rs.getFloat("latitude")
                                ,rs.getInt("longitude"))
                       );
                listOfDepots.add(depot);
            }
            //Get Drones
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT droneId, droneName, droneTypeID, droneStatus, droneDepotID FROM drones");

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
                Drone drone = new Drone(
                        rs.getInt("droneId")
                        , rs.getString("droneName")
                        ,droneType
                        ,rs.getInt("droneStatus"),depot);
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
            vehicleBuilder.setStartLocation(drone.getDepot().getLocation().toJspritLocation());
            vehicleBuilder.setType(drone.getDroneType().toJspritVehicleType());
            VehicleImpl vehicle = vehicleBuilder.build();
            listOfVehicles.add(vehicle);
        }
    }

    private void createVRPServices()
    {
        // For each order, create a corresponding Service in Jsprit
        //TODO auslagern in Order
        for (Order o: listOfOrders)
        {
            Service serv;
            Service.Builder servBuilder = Service.Builder.newInstance(Integer.toString(o.getOrderId()));
            servBuilder.addSizeDimension(DroneType.WEIGHT_INDEX, o.getWeight());
            servBuilder.setLocation(o.getLocation().toJspritLocation());
            serv = servBuilder.build();
            listOfServices.add(serv);
        }
    }

    private VehicleRoutingProblemSolution solveVRPProblem()
    {
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
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder;
        costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

        // See VehicleRoutingTransportCosts implementations!!!!
        //VehicleRoutingTransportCosts transCost = new EuclideanCosts();

        //vrpBuilder.setRoutingCost(transCost);

        VehicleRoutingProblem problem = vrpBuilder.build();

        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        // Debug
        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);

        return bestSolution;
    }

}
