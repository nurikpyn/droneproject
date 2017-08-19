package de.reekind.droneproject.filter;

import java.io.IOException;
import java.sql.*;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 * Created by timbe on 11.08.2017.
 */
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

        //return username.equals("info@reekind.de");
        return AuthenticateUser(username, password);
    }

    /**
     * Authenticates User against DB
     * @param email emailaderess
     * @param password password
     * @return true if authorized
     */
    public boolean AuthenticateUser(String email, String password) {

        return (email.equals("info@reekind.de")  && password.equals("test"));

    }
}
