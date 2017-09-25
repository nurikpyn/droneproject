package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Depot;
import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.DroneType;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class DroneDAO {

    private static final Map<Integer, Drone> droneMap = new HashMap<>();
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    private static void init() {
        _log.info("Lade Drohnen aus Datenbank");
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT droneId " +
                    ", droneTypeId, droneStatus, droneDepotId " +
                    "FROM drones");

            while (rs.next()) {
                DroneType droneType = DroneTypeDAO.getDroneType(rs.getInt("droneTypeID"));
                Depot depot = DepotDAO.getDepot(rs.getInt("droneDepotID"));
                Drone drone = new Drone(
                        rs.getInt("droneId")
                        , droneType
                        , rs.getInt("droneStatus")
                        , depot);
                droneMap.put(drone.getDroneId(), drone);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
    }

    /**
     * Auflisten aller Drohnen
     *
     * @return Liste aller Drohnen
     */
    public static List<Drone> getAllDrones() {
        Collection<Drone> droneCollection = droneMap.values();
        List<Drone> list = new ArrayList<>();
        list.addAll(droneCollection);
        return list;
    }
    public static List<Drone> getDronesWithStatus(DroneStatus droneStatus) {
        Collection<Drone> droneCollection = droneMap.values();
        List<Drone> list = new ArrayList<>();
        for(Drone drone : droneCollection) {
            if (drone.getDroneStatus() == droneStatus)
                list.add(drone);
        }
        return list;
    }

    /**
     * Gebe Drohne mit angegebener Id zurück
     *
     * @param droneId Id der Drohne
     * @return Drohnenobjekt
     */
    public static Drone getDrone(Integer droneId) {
        return droneMap.get(droneId);
    }

    /**
     * Hinzufügen von Drohnen
     *
     * @param drone Drohne, welche hinzugefügt wird
     * @return Drohne, welche hinzugefügt wird
     * @throws SQLException SQL Fehler
     */
    public static Drone addDrone(Drone drone) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO drones (droneTypeId, droneStatus, droneDepotId) " +
                "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        if (drone.getDroneType() != null)
            statement.setInt(1, drone.getDroneType().getDroneTypeId());
        else
            statement.setInt(1, 1);
        statement.setInt(2, drone.getDroneStatus().GetID());
        if (drone.getDepot() != null)
            statement.setInt(3, drone.getDepot().getDepotID());
        else
            statement.setInt(3, 1);

        statement.execute();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.first()) {
            int generatedKey = generatedKeys.getInt(1);
            drone.setDroneId(generatedKey);
        } else {
            throw new SQLException("Creating drone failed, no ID obtained.");
        }
        droneMap.put(drone.getDroneId(), drone);
        return drone;
    }

    /**
     * Aktualisieren von Drohnendaten in der Map und in der Datenbank
     *
     * @param drone Drohne mit aktualisierten Daten
     * @return Dasselbe Objekt wie beim Input
     */
    public static Drone updateDrone(Drone drone) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement(
                    "UPDATE drones SET droneTypeId = ?, droneStatus = ?" +
                            ", droneDepotId = ? " +
                            "WHERE droneId = ?");
            if (drone.getDroneType() != null)
                statement.setInt(1, drone.getDroneType().getDroneTypeId());
            else
                statement.setInt(1, 1);
            statement.setInt(2, drone.getDroneStatus().GetID());
            if (drone.getDepot() != null)
                statement.setInt(3, drone.getDepot().getDepotID());
            else
                statement.setInt(3, 1);
            statement.setInt(4, drone.getDroneId());
            statement.execute();
        } catch (SQLException e) {
            _log.error("Fehler beim Updaten der Drohne", e);
        }
        droneMap.put(drone.getDroneId(), drone);
        return drone;
    }

    /**
     * Löschen von Drohnen
     *
     * @param droneId Id der Drohne, welche gelöscht werden soll
     */
    public static void deleteDrone(Integer droneId) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM drones WHERE droneId = ?");
            statement.setInt(1, droneId);
            statement.execute();
            droneMap.remove(droneId);
        } catch (SQLException e) {
            _log.error(String.format("Fehler beim Löschen der Drohne %s", droneId), e);
        }
    }
}