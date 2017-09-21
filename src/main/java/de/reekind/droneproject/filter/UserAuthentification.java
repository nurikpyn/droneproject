package de.reekind.droneproject.filter;

import de.reekind.droneproject.dao.UserDAO;
import de.reekind.droneproject.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 * Created by timbe on 11.08.2017.
 */
public class UserAuthentification {
    private final static Logger _log = LogManager.getLogger();
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
        if (usernameAndPassword == null) {
            return false;
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        return (AuthenticateUser(username, password));
    }

    /**
     * Authenticates User against DB
     *
     * @param username username
     * @param password password
     * @return true if authorized
     */
    private boolean AuthenticateUser(String username, String password) {
        User user = UserDAO.getUser(username);
        return (username.equals(user.getUsername()) && password.equals(user.getPassword()));

    }
}
