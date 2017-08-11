package de.reekind.droneproject.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by timbe on 11.08.2017.
 */
@Path("/api/authentification")
public class UserAuthentification {
    Connection conn;
    @GET
    //@Compress //can be used only if you want to SELECTIVELY enable compression at the method level. By using the EncodingFilter everything is compressed now.
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    /**
     * Authenticates User against DB
     * @param email
     * @param passwordHash
     * @return Token or Null if failure. Token contains userID, userType, random chars and the checksum of all this encrypted
     */
    public String AuthenticateUser(String email, String passwordHash) {

        try {
            conn =  DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                    "user=reekind_dronepr&password=NW4LcAQYV195");

            //use Prepared Statements to protect against SQL Injection
            PreparedStatement getUsersStatement = conn.prepareStatement("SELECT userID, userType FROM users" +
                    " WHERE email = ? AND passwordHash = ?");
            getUsersStatement.setString(1,email);
            getUsersStatement.setString(2,passwordHash);
            ResultSet getUserResult = getUsersStatement.executeQuery("SELECT userID, userType FROM users");
            conn.commit();

            //Jump to first row. If no user is found, returns false
            if (getUserResult.first()) {
                String token;
                int userID = getUserResult.getInt(1);
                token = String.format("%0" + Integer.toString(userID).length()+ "d", userID);
                int userType = getUserResult.getInt(2);
                token += userType;
                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int randomNum = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
                token += randomNum;
                String hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(token);
                token += hash;
                return token;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }

    }
}
