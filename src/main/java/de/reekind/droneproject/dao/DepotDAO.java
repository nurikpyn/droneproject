package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Depot;
import de.reekind.droneproject.model.DroneType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepotDAO {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Laden eines bestimmten Depots
     *
     * @param depotId Depot-Id
     * @return Depot
     */
    public static Depot getDepot(Integer depotId) {
        Depot depot = null;
        PreparedStatement stmt = null;
        ResultSet resultSet;
        List<DroneType> list = new ArrayList<>();
        try {
            stmt = dbConnection.prepareStatement("SELECT depots.depotId, depots.depotName, depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "WHERE depots.depotID =  ?");
            stmt.setInt(1, depotId);
            resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                depot = new Depot(
                        resultSet.getInt("depotId")
                        , resultSet.getString("depotName")
                        , new de.reekind.droneproject.model.Location(
                        resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude")));
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
        return depot;
    }

    /**
     * Laden aller Depots
     *
     * @return Liste mit allen Depots
     */
    public static List<Depot> getAllDepots() {
        Statement stmt = null;
        ResultSet resultSet;
        List<Depot> list = new ArrayList<>();
        try {
            stmt = dbConnection.createStatement();
            resultSet = stmt.executeQuery("SELECT depots.depotId, depots.depotName" +
                    ", depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "LEFT JOIN drones ON depots.depotId = drones.droneDepotId " +
                    "WHERE drones.droneDepotId IS NOT NULL " +
                    "GROUP BY drones.droneDepotId");
            while (resultSet.next()) {
                Depot depot = new Depot(
                        resultSet.getInt("depotId")
                        , resultSet.getString("depotName")
                        , new de.reekind.droneproject.model.Location(
                        resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude"))
                );
                list.add(depot);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
        return list;
    }
}