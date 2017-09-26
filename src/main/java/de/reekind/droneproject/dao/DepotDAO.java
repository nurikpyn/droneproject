package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Depot;
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
    public static Depot getDepot(int depotId) {
        Depot depot = null;
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT depots.depotId, depots.depotName, depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "WHERE depots.depotID =  ?");
            preparedStatement.setInt(1, depotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                depot = new Depot(
                        resultSet.getInt("depotId")
                        , resultSet.getString("depotName")
                        , resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude"));
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
        List<Depot> list = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT depots.depotId, depots.depotName" +
                    ", depots.latitude, depots.longitude " +
                    "FROM depots " +
                    "LEFT JOIN drones ON depots.depotId = drones.droneDepotId " +
                    "WHERE drones.droneDepotId IS NOT NULL " +
                    "GROUP BY drones.droneDepotId");
            while (resultSet.next()) {
                Depot depot = new Depot(
                        resultSet.getInt("depotId")
                        , resultSet.getString("depotName")
                        , resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude")
                );
                list.add(depot);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
        return list;
    }
}