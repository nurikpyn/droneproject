package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Location;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LocationDAO {
    private static final Map<Integer, Location> locationMap = new HashMap<>();
    private static Connection dbConnection;
    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    /**
     * Lade Orte beim Start der Applikation
     */
    private static void init() {
        try
        {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT locations.locationId " +
                    ", locations.name, locations.latitude" +
                    ", locations.longitude " +
                    "FROM locations " +
                    "ORDER BY locationId ASC");

            //Füge einzelne Orte in DAO/Map ein
            while (rs.next()) {
                Location location = new Location(rs.getInt("locationId"), rs.getString("name")
                        , rs.getDouble("latitude"), rs.getDouble("longitude"));
                locationMap.put(location.locationId, location);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gebe Location mit angegebener LocationId zurück
     * @param locationId LocationId
     * @return Location mit angegebener LocationId
     */
    public static Location getLocation(Integer locationId) {
        return locationMap.get(locationId);
    }

    public static Location addLocation(Location location) {
        try {
            String sqlStatement;
            PreparedStatement preparedStatement;

            if (location.locationId != 0) {

                sqlStatement = "INSERT INTO locations (locationId, name, latitude, longitude) VALUES (?,?,?,?)";

                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement);
                preparedStatement.setInt(1, location.locationId);
                preparedStatement.setString(2,location.getName());
                preparedStatement.setDouble(3,location.latitude);
                preparedStatement.setDouble(4,location.longitude);
                preparedStatement.execute();
            } else { //Automatische Berechnung der AdressId
                sqlStatement = "INSERT INTO locations (name, latitude, longitude) VALUES (?,?,?)";
                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1,location.getName());
                preparedStatement.setDouble(2,location.latitude);
                preparedStatement.setDouble(3,location.longitude);
                preparedStatement.execute();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        location.locationId = (int)generatedKeys.getLong(1);
                    }
                    else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }


        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        //Füge Location in DAO ein
        locationMap.put(location.locationId, location);

        return location;
    }
}
