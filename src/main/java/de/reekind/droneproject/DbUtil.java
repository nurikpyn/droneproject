package de.reekind.droneproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtil {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection = null;

    public static Connection getConnection() {
        try {
            if (dbConnection != null && dbConnection.isValid(1)) {
                return dbConnection;
            } else {

                InputStream inputStream = DbUtil.class.getClassLoader()
                        .getResourceAsStream("db.properties");
                Properties properties = new Properties();
                properties.load(inputStream);

                String dbDriver = properties.getProperty("dbDriver");
                String connectionUrl = properties
                        .getProperty("connectionUrl");
                String userName = properties.getProperty("userName");
                String password = properties.getProperty("password");

                Class.forName(dbDriver).newInstance();
                dbConnection = DriverManager.getConnection(connectionUrl,
                        userName, password);
            }
        } catch (Exception e) {
            _log.error("Fehler beim Laden der Datenbankverbindung", e);
        }
        return dbConnection;
    }
}
