package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.UserRole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRoleDAO {
    private static Connection dbConnection;
    static {
        dbConnection = DbUtil.getConnection();
    }

    public static UserRole getUserRole(Integer userRoleId) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT userRoleId, userRoleName FROM userroles");
            if (resultSet.first()){
                return new UserRole(resultSet.getInt("userRoleid"),resultSet.getString("userRoleName"));
            } else {
                return null; //TODO Really null and not exceptoipm?
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
