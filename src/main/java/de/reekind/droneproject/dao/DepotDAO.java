package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DepotDAO {
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    public static Depot getDepot(Integer depotId) {
        Depot depot = null;
        PreparedStatement stmt = null;
        ResultSet resultSet;
        List<DroneType> list = new ArrayList<>();
        try {
            stmt = dbConnection.prepareStatement("SELECT depots.depotID, depots.name, depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "WHERE depots.depotID =  ?");
            stmt.setInt(1,depotId);
            resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                depot = new Depot(
                        resultSet.getInt("depotID")
                        , resultSet.getString("name")
                        ,new de.reekind.droneproject.model.Location(
                        resultSet.getFloat("latitude")
                        ,resultSet.getInt("longitude")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return depot;
    }

    public static List<Depot> getAllDepots() {
        Statement stmt = null;
        ResultSet resultSet;
        List<Depot> list = new ArrayList<>();
        try {
            stmt = dbConnection.createStatement();
            resultSet = stmt.executeQuery("SELECT depots.depotID, depots.name, depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "LEFT JOIN drones ON depots.depotID = drones.droneDepotID " +
                    "WHERE drones.droneDepotID IS NOT NULL " +
                    "GROUP BY drones.droneDepotID");
            while (resultSet.next()) {
                Depot depot = new Depot(
                        resultSet.getInt("depotID")
                        , resultSet.getString("name")
                        ,new de.reekind.droneproject.model.Location(
                        resultSet.getFloat("latitude")
                        ,resultSet.getInt("longitude"))
                );
               list.add(depot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}