package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.DroneType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DroneTypeDAO {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Laden von bestimmtem Drohnentyp
     *
     * @param droneTypeId Id des Drohnentyps
     * @return Drohnentyp
     */
    public static DroneType getDroneType(Integer droneTypeId) {
        DroneType droneType = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT droneTypeId, droneTypeName," +
                    " maxWeightInGrams, maxPackageCount, maxRange, speed " +
                    "FROM dronetypes " +
                    "WHERE droneTypeID = ?");
            statement.setInt(1, droneTypeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                droneType = new DroneType(
                        resultSet.getInt("droneTypeId")
                        , resultSet.getString("droneTypeName")
                        , resultSet.getInt("maxWeightInGrams")
                        , resultSet.getInt("maxPackageCount")
                        , resultSet.getFloat("maxRange")
                        , resultSet.getInt("speed")
                );
            }
        } catch (SQLException e) {
            _log.error(String.format("Fehler beim laden von Drohnentyp %d", droneTypeId), e);
        }
        return droneType;
    }

    /**
     * Laden von allen vorhandenen Drohnentypen
     *
     * @return Liste mit allen Drohnentypen
     */
    public static List<DroneType> getAllDroneTypes() {
        List<DroneType> list = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT dronetypes.droneTypeId, dronetypes.droneTypeName," +
                    " maxWeightInGrams, maxPackageCount, dronetypes.maxRange, dronetypes.speed " +
                    "FROM dronetypes " +
                    "GROUP BY droneTypeId");
            while (resultSet.next()) {
                DroneType droneType = new DroneType(
                        resultSet.getInt("droneTypeId")
                        , resultSet.getString("droneTypeName")
                        , resultSet.getInt("maxWeightInGrams")
                        , resultSet.getInt("maxPackageCount")
                        , resultSet.getFloat("maxRange")
                        , resultSet.getInt("speed")
                );
                list.add(droneType);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim laden der Drohnentypen", e);
        }
        return list;
    }
}