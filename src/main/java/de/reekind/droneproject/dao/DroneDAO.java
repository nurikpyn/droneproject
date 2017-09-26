package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Depot;
import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.DroneType;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DroneDAO {

    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Auflisten aller Drohnen
     *
     * @return Liste aller Drohnen
     */
    public static List<Drone> getAllDrones() {
        List<Drone> droneList = new ArrayList<>();
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
                droneList.add(drone);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
        return droneList;
    }

    public static List<Drone> getDronesWithStatus(DroneStatus droneStatus) {
        Collection<Drone> droneCollection = getAllDrones();
        List<Drone> list = new ArrayList<>();
        for (Drone drone : droneCollection) {
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
        Drone drone = null;
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT droneId " +
                    ", droneTypeId, droneStatus, droneDepotId " +
                    "FROM drones WHERE droneID = ?");
            preparedStatement.setInt(1, droneId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                DroneType droneType = DroneTypeDAO.getDroneType(resultSet.getInt("droneTypeID"));
                Depot depot = DepotDAO.getDepot(resultSet.getInt("droneDepotID"));
                drone = new Drone(
                        resultSet.getInt("droneId")
                        , droneType
                        , resultSet.getInt("droneStatus")
                        , depot);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Depots", e);
        }
        return drone;
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
        return drone;
    }

    /**
     * Löschen von Drohnen
     *
     * @param droneId Id der Drohne, welche gelöscht werden soll
     */
    public static void deleteDrone(Integer droneId) {
        Drone drone = DroneDAO.getDrone(droneId);
        if (drone.getDroneStatus() == DroneStatus.InAuslieferung) {
            _log.error("Drohne {} kann nicht entfernt werden, da sie gerade in Auslieferung ist.", droneId);
            return;
        }

        try {
            PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM drones WHERE droneId = ?");
            statement.setInt(1, droneId);
            statement.execute();
        } catch (SQLException e) {
            _log.error(String.format("Fehler beim Löschen der Drohne %s", droneId), e);
        }
    }
}