package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    public static User getUser(Integer userId) {
        User user = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT userId, name, password, userRoleId " +
                    "FROM users WHERE userId = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getInt("userID")
                        , resultSet.getString("name")
                        , resultSet.getString("password")
                        , UserRoleDAO.getUserRole(resultSet.getInt("userRoleId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUser(String userName) {
        User user = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT userId, name, password, userRoleId " +
                    "FROM users WHERE name = ?");
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getInt("userID")
                        , resultSet.getString("name")
                        , resultSet.getString("password")
                        , UserRoleDAO.getUserRole(resultSet.getInt("userRoleId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT userId, name, password, userRoleId FROM users");
            while (resultSet.next()) {
                User user = new User(resultSet.getInt("userID")
                        , resultSet.getString("name")
                        , resultSet.getString("password")
                        , UserRoleDAO.getUserRole(resultSet.getInt("userRoleId")));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
