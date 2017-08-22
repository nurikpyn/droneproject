package de.reekind.droneproject.dao;

import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DroneDAO {

    private static final Map<Integer, Drone> droneMap = new HashMap<>();
    private static final Map<Integer, DroneType> droneTypeMap = new HashMap<>();
    private static final Map<Integer,Depot> depotMap = new HashMap<>();

    static {
        initDrones();
    }

    private static void initDrones() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT dronetypes.droneTypeId, maxWeightInGrams, maxPackageCount, dronetypes.maxRange " +
                    "FROM dronetypes " +
                    "LEFT JOIN drones ON dronetypes.droneTypeId = drones.droneTypeId " +
                    "WHERE drones.droneTypeID IS NOT NULL " +
                    "GROUP BY droneTypeId");
            //Get dronetypes
            // Join to only get the relevant types
            while (rs.next()) {
                DroneType droneType = new DroneType(rs.getInt("droneTypeId"),
                        rs.getInt("maxWeightInGrams"),
                        rs.getInt("maxPackageCount"),
                        rs.getInt("maxRange"));
                droneTypeMap.put(droneType.getDroneTypeId(), droneType);
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
                        new Location(rs.getDouble(2), rs.getDouble(3))
                );
                depotMap.put(depot.getDepotID(), depot);
            }
            //Get Drones
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT droneId, droneTypeID, droneStatus, droneDepotID FROM drones");

            while (rs.next()) {
                Drone drone = new Drone(rs.getInt("droneId")
                        , droneTypeMap.get(rs.getInt("droneTypeID"))
                        , rs.getInt("droneStatus")
                        , depotMap.get(rs.getInt("droneDepotID"))
                );
                droneMap.put(drone.getDroneId(), drone);
            }
            // Now we have all drones in our data structure

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        //TODO Lade bestehende Dronen aus der Datenbank
        Drone drone1 = new Drone(1, new DroneType(), 1, new Depot(1, new Location(5.1, 3.4)));
        Drone drone2 = new Drone(2, new DroneType(), 1, new Depot(1, new Location(5.1, 3.4)));
        Drone drone3 = new Drone(3, new DroneType(), 1, new Depot(1, new Location(5.1, 3.4)));
        Drone drone4 = new Drone(4, new DroneType(), 1, new Depot(1, new Location(5.1, 3.4)));

        droneMap.put(drone1.getDroneId(), drone1);
        droneMap.put(drone2.getDroneId(), drone2);
        droneMap.put(drone3.getDroneId(), drone3);
        droneMap.put(drone4.getDroneId(), drone4);
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