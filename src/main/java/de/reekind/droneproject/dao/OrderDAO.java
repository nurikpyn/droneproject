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

    /**
     * Lade Bestellungen beim Start der Applikation
     */
    private static void initOrders() {
        try
        {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT orderId, orderTime" +
                    ", adresses.adress, adresses.latitude" +
                    ", adresses.longitude, weight, orderStatus, droneId " +
                    "FROM orders " +
                    "JOIN adresses ON adresses.adressID = orders.adressID " +
                    "ORDER BY orderId ASC");

            //Füge einzelne Bestellungen in DAO/Map ein
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("orderId")
                        , rs.getTimestamp("orderTime")
                        , new Location(rs.getString("adress"), rs.getDouble("latitude"), rs.getDouble("longitude"))
                        , rs.getInt("weight")
                        , rs.getInt("orderStatus"));
                orderMap.put(order.getOrderId(), order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gebe Bestellung mit angegebener OrderId zurück
     * @param orderId Bestellnummer
     * @return Bestellung mit angegebener Bestellnummer
     */
    public static Order getOrder(Integer orderId) {
        return orderMap.get(orderId);
    }

    /**
     * Füge neue Bestellung in DAO/Map ein
     * @param order Bestellung, welche eingefügt werden soll.
     * @return Bestellung als DAO Element
     */
    public static Order addOrder(Order order) {
        try {
            String sqlStatement;
            PreparedStatement preparedStatement;

            Location _location = order.getLocation();
            if(_location != null && _location.locationId == 0) {
                if (_location.longitude == 0 && _location.latitude == 0) {
                    if (_location.getName() != null) {
                        _location.getCoordinatesFromAdress(_location.getName());
                    }
                }
            }

            if (order.getOrderId() != 0) {

                sqlStatement = "INSERT INTO orders (orderID, orderTime, adressID, weight, orderStatus, droneID) VALUES (?,?,?,?,?,?)";

                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement);
                preparedStatement.setInt(1,order.getOrderId());
                if(order.getOrderTime() != null)
                    preparedStatement.setTimestamp(2, order.getOrderTime());
                else
                    preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

                if (order.getLocation() != null)
                    preparedStatement.setInt(3,order.getLocation().locationId);
                else
                    preparedStatement.setInt(3, 0);

                preparedStatement.setInt(4,order.getWeight());

                if (order.getStatus() != null)
                    preparedStatement.setInt(5,order.getStatus().GetID());
                else
                    preparedStatement.setInt(5,0);

                if (order.getDrone() != null)
                    preparedStatement.setInt(6,order.getDrone().getDroneId());
                else
                    preparedStatement.setInt(6,0);

                preparedStatement.execute();

            } else { //Automatische Berechnung der Bestellnummer
                sqlStatement = "INSERT INTO orders (orderTime, adressID, weight, orderStatus, droneID) VALUES (?,?,?,?,?)";
                preparedStatement = dbConnection.prepareStatement(
                        sqlStatement, Statement.RETURN_GENERATED_KEYS);

                if(order.getOrderTime() != null)
                    preparedStatement.setTimestamp(1, order.getOrderTime());
                else
                    preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

                preparedStatement.setInt(2,order.getLocation().locationId);

                preparedStatement.setInt(3,order.getWeight());

                if (order.getStatus() != null)
                    preparedStatement.setInt(4,order.getStatus().GetID());
                else
                    preparedStatement.setInt(4,0);

                if (order.getDrone() != null)
                    preparedStatement.setInt(5,order.getDrone().getDroneId());
                else
                    preparedStatement.setInt(5,0);

                preparedStatement.execute();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(((int) generatedKeys.getLong(1)));
                    }
                    else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            } //order.getOrderId() == 0


        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        //Füge Drohne in DAO ein
        orderMap.put(order.getOrderId(), order);

        return order;
    }

    /**
     * Aktualisiere bestehende Bestellung
     * @param order zu aktualisierende Bestellung
     * @return Aktualisierte Bestellung
     */
    public static Order updateOrder(Order order) {
        orderMap.put(order.getOrderId(), order);
        //TODO Schreibe  Drohne in DB
        return order;
    }

    /**
     * Lösche Bestellung mit angegebener Bestellnummer
     * @param orderId Bestellnummer
     */
    public static void deleteOrder(Integer orderId) {
        //TODO Lösche Drohne in DB
        orderMap.remove(orderId);
    }

    /**
     * Lade alle bestehenden Bestellungen
     * @return List<Orders> mit allen Bestellungen
     */
    public static List<Order> getAllOrders() {
        Collection<Order> c = orderMap.values();
        List<Order> list = new ArrayList<>();
        list.addAll(c);
        //Collections.sort(list);
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