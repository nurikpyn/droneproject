package de.reekind.droneproject;

import de.reekind.droneproject.dao.LocationDAO;
import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.service.OrderService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SampleOrderTest {


    public static void main(String[] args)
    {
        try {
            Connection conn = DbUtil.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT orderTime, deliveryPlace, weight * 1000 FROM sample_orders");

            while (rs.next()) {
                Location xx = new Location(rs.getString(2));
                LocationDAO.addLocation(xx);


                Order order = new Order( rs.getTimestamp(1)
                        , xx
                        , rs.getInt(3));
                OrderDAO.addOrder(order);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
