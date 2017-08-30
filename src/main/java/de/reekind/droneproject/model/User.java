package de.reekind.droneproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    private int UserId;
    private String Username;
    private String Password;
    private UserRole UserRole;

    public User() {

    }
    public User(int userId, String username, String password, UserRole userRole) {
        this.UserId = userId;
        this.Username = username;
        this.Password = password;
        this.UserRole = userRole;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public UserRole getUserRole() {
        return UserRole;
    }

    public void setUserRole(de.reekind.droneproject.model.UserRole userRole) {
        UserRole = userRole;
    }
}
