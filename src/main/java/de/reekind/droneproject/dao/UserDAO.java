package de.reekind.droneproject.dao;

import de.reekind.droneproject.model.User;
import de.reekind.droneproject.model.UserRole;

import java.util.*;

public class UserDAO {
    private static final Map<Integer, User> userMap = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        //TODO Lade bestehende Usern aus der Datenbank
        UserRole role = new UserRole();


        User user1 = new User(1,"test", "test", role);

        userMap.put(user1.getUserId(), user1);
    }

    public static User getUser(Integer userId) {
        return userMap.get(userId);
    }

    public static User addUser(User user) {
        userMap.put(user.getUserId(), user);
        //TODO Schreibe neue Drohne in DB
        return user;
    }

    public static User updateUser(User user) {
        userMap.put(user.getUserId(), user);
        //TODO Schreibe  Drohne in DB
        return user;
    }

    public static void deleteUser(Integer userId) {
        //TODO Lösche Drohne in DB
        userMap.remove(userId);
    }

    public static List<User> getAllUsers() {
        Collection<User> c = userMap.values();
        List<User> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }
}
