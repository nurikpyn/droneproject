package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.*;
import de.reekind.droneproject.model.enumeration.OrderStatus;

import java.sql.*;
import java.util.*;

public class OrderDAO {

    private static final Map<Integer, Order> orderMap = new HashMap<>();
    private static Connection dbConnection;
    static {
        dbConnection = DbUtil.getConnection();
        initOrders();
    }

    private static void initOrders() {
        try
        {
            //Get Orders
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT orderId, orderTime" +
                    ", adresses.latitude" +
                    ", adresses.longitude, weight, orderStatus, droneId " +
                    "FROM orders " +
                    "JOIN adresses ON adresses.adressID = orders.adressID " +
                    "ORDER BY orderId ASC");

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderId")
                        , rs.getTimestamp("orderTime")
                        , rs.getDouble("latitude")
                        , rs.getDouble("longitude")
                        , rs.getInt("weight")
                        , rs.getInt("orderStatus"));
                orderMap.put(order.getOrderId(), order);
            }
            // Now we have all orders in our data structure

            // Do something with the Connection
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Order getOrder(Integer orderId) {
        return orderMap.get(orderId);
    }

    public static Order addOrder(Order order) {
        //TODO Schreibe neue Drohne in DB
        try {
            String sqlStatement;
            PreparedStatement preparedStatement;
            if (order.getOrderId() != 0) {
                sqlStatement = "INSERT INTO orders (orderID, orderTime, adressID, weight, orderStatus, droneID) VALUES (?,?,?,?,?,?)";

                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement);
                preparedStatement.setInt(1,order.getOrderId());
                if(order.getOrderTime() != null)
                    preparedStatement.setTimestamp(2, order.getOrderTime());
                else
                    preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

                preparedStatement.setInt(3,order.getLocation().locationId);
                preparedStatement.setInt(4,order.getWeight());
                preparedStatement.setInt(5,0);
                preparedStatement.setInt(6,order.getDroneId());
            } else {
                sqlStatement = "INSERT INTO orders (orderTime, adressID, weight, orderStatus, droneID) VALUES (?,?,?,?,?)";
                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement, Statement.RETURN_GENERATED_KEYS);
                if(order.getOrderTime() != null)
                    preparedStatement.setTimestamp(1, order.getOrderTime());
                else
                    preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

                preparedStatement.setInt(2,order.getLocation().locationId);
                preparedStatement.setInt(3,order.getWeight());
                preparedStatement.setInt(4,0);
                preparedStatement.setInt(5,order.getDroneId());
            }

            preparedStatement.execute();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(((int) generatedKeys.getLong(1)));
                }
                else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        //Füge Drohne in DAO ein
        orderMap.put(order.getOrderId(), order);

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
    public static List<Order> getOrdersWithStatus(OrderStatus _status) {
        Collection<Order> orderCollection = orderMap.values();
        List<Order> list = new ArrayList<>();
        orderCollection.forEach((Order order) ->{
            if (order.getStatus() == _status) {
                list.add(order);
            }
        });

        return list;
    }
}