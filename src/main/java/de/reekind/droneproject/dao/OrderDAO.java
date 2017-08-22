package de.reekind.droneproject.dao;

import de.reekind.droneproject.model.*;
import org.joda.time.DateTime;

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
        Order order1 = new Order(1,new Timestamp(System.currentTimeMillis()),0,"Salcombe", 2100);
        Order order2 = new Order(2,new Timestamp(System.currentTimeMillis()),0,"Thurlestone", 1200);
        Order order3 = new Order(3,new Timestamp(System.currentTimeMillis()),0,"Beesands", 700);
        Order order4 = new Order(4,new Timestamp(System.currentTimeMillis()),0,"West Charleton", 3900);
        Order order5 = new Order(5,new Timestamp(System.currentTimeMillis()),0,"Aveton Gifford", 2140);
        Order order6 = new Order(6,new Timestamp(System.currentTimeMillis()),0,"Hope", 550);


        orderMap.put(order1.getOrderId(), order1);
        orderMap.put(order2.getOrderId(), order2);
        orderMap.put(order3.getOrderId(), order3);
        orderMap.put(order4.getOrderId(), order4);
        orderMap.put(order5.getOrderId(), order5);
        orderMap.put(order6.getOrderId(), order6);
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