package de.reekind.droneproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by timbe on 11.08.2017.
 */
@Path("/api/authentification")
public class UserAuthentification {
    Connection conn;
    public boolean authenticate(String authCredentials) {

        if (null == authCredentials)
            return false;
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        return AuthenticateUser(username, password);
    }

    /**
     * Authenticates User against DB
     * @param email
     * @param password
     * @return true if authorized
     */
    public boolean AuthenticateUser(String email, String password) {

        try {
            conn =  DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");

            //use Prepared Statements to protect against SQL Injection
            PreparedStatement getUsersStatement = conn.prepareStatement("SELECT userID, userType FROM users" +
                    " WHERE email = ? AND passwordHash = ?");
            getUsersStatement.setString(1,email);
            getUsersStatement.setString(2,org.apache.commons.codec.digest.DigestUtils.sha256Hex(password));
            ResultSet getUserResult = getUsersStatement.executeQuery("SELECT userID, userType FROM users");
            conn.commit();

            //Jump to first row. If no user is found, returns false
            if (getUserResult.first()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }

    }
}
