package de.reekind.droneproject.rest;

import de.reekind.droneproject.domain.Drone;

import java.sql.*;
import java.util.*;

/**
 * Created by timbe on 07.08.2017.
 */
public class LoadDataFromDB {
    public static void main (String[] args) {
        Connection conn = null;
        List<Drone> droneList = new ArrayList<Drone>();

        try {

            conn =  DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
                            "user=reekind_dronepr&password=NW4LcAQYV195");
            Statement stmt = null;
            ResultSet rs = null;

            //Get Drones
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT droneID, droneTypeID, status from drones");

            while (rs.next()) {
                // drone = new Drone(rs.getInt(1));
                //droneList.add(drone);
            }

            for (Drone drone : droneList) {
               // System.out.println(drone.getID());
            }

            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
