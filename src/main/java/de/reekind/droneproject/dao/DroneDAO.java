package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DroneDAO {

    private static final Map<Integer, Drone> droneMap = new HashMap<>();
    private static final Map<Integer, DroneType> droneTypeMap = new HashMap<>();
    private static final Map<Integer, Depot> depotMap = new HashMap<>();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    private static void init() {
        try
        {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT dronetypes.droneTypeId, dronetypes.droneTypeName," +
                    " maxWeightInGrams, maxPackageCount, dronetypes.maxRange, dronetypes.speed " +
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
                        ,rs.getFloat("maxRange")
                , rs.getInt("speed"));
                droneTypeMap.put(droneType.getDroneTypeId(), droneType);
            }
            //Get Depots
            // Join to only get the relevant types
            stmt = dbConnection.createStatement();
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
                depotMap.put(depot.getDepotID(), depot);
            }
            //Get Drones
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT droneId, droneName, droneTypeID, droneStatus, droneDepotID FROM drones");

            while (rs.next()) {
                DroneType droneType = droneTypeMap.get(rs.getInt("droneTypeID"));
                Depot depot = depotMap.get(rs.getInt("droneDepotID"));
                Drone drone = new Drone(
                        rs.getInt("droneId")
                        , rs.getString("droneName")
                        ,droneType
                        ,rs.getInt("droneStatus")
                        ,depot);
                droneMap.put(drone.getDroneId(), drone);
            }
            // Now we have all orders in our data structure

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

/*
        //TODO Lade bestehende Dronen aus der Datenbank
        Depot depotSalcombe = new Depot(1, "Salcombe", new Location(5.1, 3.4));
        Drone drone1 = new Drone(1, "Capt. America", new DroneType(), 1, depotSalcombe);
        Drone drone2 = new Drone(2, "Thor", new DroneType(), 1, depotSalcombe);
        Drone drone3 = new Drone(3, "Hulk", new DroneType(), 1, depotSalcombe);
        Drone drone4 = new Drone(4, "Spider Man", new DroneType(), 1, depotSalcombe);
        Drone drone5 = new Drone(5, "Warriour", new DroneType(), 1, depotSalcombe);

        droneMap.put(drone1.getDroneId(), drone1);
        droneMap.put(drone2.getDroneId(), drone2);
        droneMap.put(drone3.getDroneId(), drone3);
        droneMap.put(drone4.getDroneId(), drone4);
        droneMap.put(drone5.getDroneId(), drone5);*/
    }

    public static Drone getDrone(Integer droneId) {
        return droneMap.get(droneId);
    }

    public static Drone addDrone(Drone drone) {
        droneMap.put(drone.getDroneId(), drone);
        //TODO Schreibe neue Drohne in DB
        return drone;
    }

    public static Drone updateDrone(Drone drone) {
        droneMap.put(drone.getDroneId(), drone);
        //TODO Schreibe  Drohne in DB
        return drone;
    }

    public static void deleteDrone(Integer droneId) {
        //TODO Lösche Drohne in DB
        droneMap.remove(droneId);
    }

    public static List<Drone> getAllDrones() {
        Collection<Drone> c = droneMap.values();
        List<Drone> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }

    List<Drone> list;

}