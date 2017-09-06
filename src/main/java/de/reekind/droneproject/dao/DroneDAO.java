package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DroneDAO {

    private static final Map<Integer, Drone> droneMap = new HashMap<>();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    private static void init() {
        try
        {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT droneId, droneName" +
                    ", droneTypeId, droneStatus, droneDepotId " +
                    "FROM drones");

            while (rs.next()) {
                DroneType droneType = DroneTypeDAO.getDroneType(rs.getInt("droneTypeID"));
                Depot depot = DepotDAO.getDepot(rs.getInt("droneDepotID"));
                Drone drone = new Drone(
                        rs.getInt("droneId")
                        , rs.getString("droneName")
                        ,droneType
                        ,rs.getInt("droneStatus")
                        ,depot);
                droneMap.put(drone.getDroneId(), drone);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Drone getDrone(Integer droneId) {
        return droneMap.get(droneId);
    }

    public static Drone addDrone(Drone drone) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO drones (droneTypeId, droneStatus, droneDepotId, droneName) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            if (drone.getDroneType() != null)
                statement.setInt(1,drone.getDroneType().getDroneTypeId());
            else
                statement.setInt(1,0);
            statement.setInt(2,drone.getDroneStatus().GetID());
            if (drone.getDepot() != null)
                statement.setInt(3,drone.getDepot().getDepotID());
            else
                statement.setInt(3,0);
            statement.setString(4,drone.getDroneName());

            statement.execute();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.first()) {
                    int generatedKey =  generatedKeys.getInt(1);
                    drone.setDroneId(generatedKey);
                }
                else {
                    throw new SQLException("Creating drone failed, no ID obtained.");
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        droneMap.put(drone.getDroneId(), drone);
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
}