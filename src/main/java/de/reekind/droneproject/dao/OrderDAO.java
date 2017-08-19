package de.reekind.droneproject.dao;

import de.reekind.droneproject.model.*;

import java.sql.*;
import java.util.*;

public class OrderDAO {

    private static final Map<Integer, Order> orderMap = new HashMap<>();

    static {
        initOrders();
    }

    private static void initOrders() {
        //try
        //{
           // Connection conn = DriverManager.getConnection("jdbc:mysql://pphvs02.reekind.de/reekind_dronepr?" +
            //        "user=reekind_dronepr&password=NW4LcAQYV195");
            //Statement stmt;
           // ResultSet rs;

        //} catch (SQLException ex) {
          //  System.out.println("SQLException: " + ex.getMessage());
       //     System.out.println("SQLState: " + ex.getSQLState());
         //   System.out.println("VendorError: " + ex.getErrorCode());
       // }
        //TODO Lade bestehende Ordern aus der Datenbank
       /* Order order1 = new Order("1", "Smith", 1, new Depot(1, new Location(5.1,3.4)));
        Order order2 = new Order("2", "Allen", 1, new Depot(1, new Location(5.1,3.4)));
        Order order3 = new Order("3", "Jones", 1,  new Depot(1, new Location(5.1,3.4)));
        Order order4 = new Order("4", "Jones", 1,  new Depot(1, new Location(5.1,3.4)));

        orderMap.put(order1.getOrderNo(), order1);
        orderMap.put(order2.getOrderNo(), order2);
        orderMap.put(order3.getOrderNo(), order3);
        orderMap.put(order4.getOrderNo(), order4);*/
    }

    public static Order getOrder(Integer orderId) {
        return orderMap.get(orderId);
    }

    public static Order addOrder(Order order) {
       orderMap.put(order.getOrderId(), order);
        //TODO Schreibe neue Drohne in DB
        return order;
    }

    public static Order updateOrder(Order order) {
       orderMap.put(order.getOrderId(), order);
        //TODO Schreibe  Drohne in DB
        return order;
    }

    public static void deleteOrder(Integer orderId) {
        //TODO Lösche Drohne in DB
        orderMap.remove(orderId);
    }

    public static List<Order> getAllOrders() {
        Collection<Order> c = orderMap.values();
        List<Order> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }
}