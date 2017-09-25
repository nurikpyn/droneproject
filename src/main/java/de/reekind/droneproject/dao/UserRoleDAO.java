package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserRoleDAO {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Laden einer bestimmten Benutzerrolle
     * @param userRoleId Zu Ladene Benutzerrolle
     * @return Geladene Benutzerrolle
     */
    public static UserRole getUserRole(Integer userRoleId) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT userRoleId, userRoleName " +
                    "FROM userroles WHERE userRoleId = ?");
            statement.setInt(1,userRoleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                return new UserRole(
                        resultSet.getInt("userRoleId")
                        , resultSet.getString("userRoleName")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Abrufen einer Benutzerrolle", e);
            return null;
        }
    }
}
