package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class DroneTypeDAO {
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    public static DroneType getDroneType(Integer droneTypeId) {
        DroneType droneType = null;
        PreparedStatement stmt = null;
        ResultSet resultSet;
        List<DroneType> list = new ArrayList<>();
        try {
            stmt = dbConnection.prepareStatement("SELECT dronetypes.droneTypeId, dronetypes.droneTypeName," +
                    " maxWeightInGrams, maxPackageCount, dronetypes.maxRange, dronetypes.speed " +
                    "FROM dronetypes " +
                    "WHERE droneTypeID = ?");
            stmt.setInt(1,droneTypeId);
            resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                droneType = new DroneType(
                        resultSet.getInt("droneTypeId")
                        ,resultSet.getString("droneTypeName")
                        ,resultSet.getInt("maxWeightInGrams")
                        ,resultSet.getInt("maxPackageCount")
                        ,resultSet.getFloat("maxRange")
                        ,resultSet.getInt("speed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return droneType;
    }

    public static List<DroneType> getAllDroneTypes() {
        Statement stmt = null;
        ResultSet resultSet;
        List<DroneType> list = new ArrayList<>();
        try {
            stmt = dbConnection.createStatement();
            resultSet = stmt.executeQuery("SELECT dronetypes.droneTypeId, dronetypes.droneTypeName," +
                    " maxWeightInGrams, maxPackageCount, dronetypes.maxRange, dronetypes.speed " +
                    "FROM dronetypes " +
                    "GROUP BY droneTypeId");
            while (resultSet.next()) {
                DroneType droneType = new DroneType(
                        resultSet.getInt("droneTypeId")
                        ,resultSet.getString("droneTypeName")
                        ,resultSet.getInt("maxWeightInGrams")
                        ,resultSet.getInt("maxPackageCount")
                        ,resultSet.getFloat("maxRange")
                        ,resultSet.getInt("speed"));
               list.add(droneType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}