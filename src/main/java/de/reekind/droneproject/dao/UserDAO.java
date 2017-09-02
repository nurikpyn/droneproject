package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.User;
import de.reekind.droneproject.model.UserRole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class UserDAO {
    private static final Map<Integer, User> userMap = new HashMap<>();
    private static Connection dbConnection;
    static {
        dbConnection = DbUtil.getConnection();
        init();
    }

    private static void init() {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT userID, name, password, userRoleId FROM users");
            while(resultSet.next()) {
                User user = new User(resultSet.getInt("userID")
                , resultSet.getString("name")
                , resultSet.getString("password")
                , new UserRole());
                userMap.put(user.getUserId(), user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(Integer userId) {
        return userMap.get(userId);
    }

    public static List<User> getAllUsers() {
        Collection<User> c = userMap.values();
        List<User> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }
}
