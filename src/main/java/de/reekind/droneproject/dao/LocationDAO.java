package de.reekind.droneproject.dao;

import com.graphhopper.jsprit.core.util.Coordinate;
import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class LocationDAO {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Gebe Location mit angegebener LocationId zurück
     *
     * @param locationId LocationId
     * @return Location mit angegebener LocationId
     */
    public static Location getLocation(int locationId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT locationId , name" +
                    ", latitude, longitude " +
                    "FROM locations " +
                    "WHERE locationId = ?");
            preparedStatement.setInt(1, locationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                return new Location(
                        resultSet.getInt("locationId")
                        , resultSet.getString("name")
                        , resultSet.getDouble("Latitude")
                        , resultSet.getDouble("longitude")
                );
            } else {
                _log.info("Keine Treffer für Ort mit locationId {}", locationId);
                return null;
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden einer Adresse anhand der Id", e);
            return null;
        }
    }

    /**
     * Lade Location mit angegebener Adresse
     *
     * @param name Adresse
     * @return Location
     */
    public static Location getLocation(String name) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT locationId , name" +
                    ", latitude, longitude " +
                    "FROM locations " +
                    "WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                return  new Location(
                        resultSet.getInt("locationId")
                        , resultSet.getString("name")
                        , resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude")
                );
            } else {
                _log.info("Keine Treffer für Ort mit Adresse {}", name);
                Location location = new Location(name);
                addLocation(location);
                return location;
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Laden der Orte", e);
            return null;
        }
    }

    /**
     * Gebe Location mit angegebenen Koordinaten zurück
     *
     * @param coordinate Latitude+ Longitude
     * @return Location mit angegebener LocationId
     */
    public static Location getLocation(Coordinate coordinate) {
        Location location = null;
        try {
            PreparedStatement preparedStatement;
            String sqlStatement = "SELECT locationId,latitude,longitude,name " +
                    "FROM locations WHERE latitude = ? AND longitude = ?";

            preparedStatement = dbConnection.prepareStatement(
                    sqlStatement);
            preparedStatement.setDouble(1, coordinate.getX());
            preparedStatement.setDouble(2, coordinate.getY());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                location = new Location(
                        resultSet.getInt("locationId")
                        , resultSet.getString("name")
                        , resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude")
                );
            } else {
                _log.error("Kein Ort zu angegebenen Koordinaten X %,d?, Y %,d gefunden", coordinate.getX(), coordinate.getY());
            }

        } catch (SQLException e) {
            _log.error("Fehler beim Laden anhand von Koordinaten",e);
        }
        return location;
    }

    /**
     * Gebe Location mit angegebenen Koordinaten zurück
     *
     * @param location location
     * @return Location mit angegebener LocationId
     */
    public static Location exists(Location location) {

        if (location.locationId != 0)
            return location;


        Location _location = null;
        try {
            PreparedStatement preparedStatement;
            String sqlStatement = "SELECT locationId, name, latitude, longitude " +
                    "FROM locations " +
                    "WHERE locationId IS NOT NULL  AND (name = ? OR (latitude = ? AND longitude = ?))";

            preparedStatement = dbConnection.prepareStatement(
                    sqlStatement);
            preparedStatement.setString(1, location.getName());
            preparedStatement.setDouble(2, location.getLatitude());
            preparedStatement.setDouble(3, location.getLongitude());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                _location = new Location(
                        resultSet.getInt("locationId")
                        , resultSet.getString("name")
                        , resultSet.getDouble("latitude")
                        , resultSet.getDouble("longitude"));
            }

        } catch (SQLException e) {
            _log.error("Fehler beim Prüfen auf Existenz",e);
        }
        return _location;
    }

    /**
     * Füge Location durch Location Objekt hinzu
     *
     * @param location Objekt der Klasse Location, welches hinzugefügt werden soll
     * @return Location mit Id
     */
    public static Location addLocation(Location location) {

        if (location.validate())
            return location;

        //Location mit angegebener Adresse oder Koordinaten bereits vorhanden
        Location _existLocation = LocationDAO.exists(location);
        //Wenn ja, dann gebe einfach diese zurück
        if (_existLocation != null)
            return _existLocation;

        try {
            PreparedStatement preparedStatement;
            preparedStatement = dbConnection.prepareStatement(
                    "INSERT INTO locations (name, latitude, longitude) " +
                            "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, location.getName());
            preparedStatement.setDouble(2, location.getLatitude());
            preparedStatement.setDouble(3, location.getLongitude());
            preparedStatement.execute();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    location.locationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating Location failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Hinzufügen von Location", e);
        }
        return location;
    }
}