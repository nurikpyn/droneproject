package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.enumeration.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class OrderDAO {

    private static final Map<Integer, Order> orderMap = new HashMap<>();
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
        initOrders();
    }

    /**
     * Lade Bestellungen beim Start der Applikation
     */
    private static void initOrders() {
        _log.debug("Lade Bestellungen aus Datenbank");
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT orderId, orderTime," +
                    " weight, orderStatus, orderRouteStopId, locationId " +
                    "FROM orders " +
                    "ORDER BY orderId ASC");

            //Füge einzelne Bestellungen in DAO/Map ein
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("orderId")
                        , new DateTime(resultSet.getTimestamp("orderTime"))
                        , LocationDAO.getLocation(resultSet.getInt("locationId"))
                        , resultSet.getInt("weight")
                        , resultSet.getInt("orderStatus")
                        , resultSet.getInt("orderRouteStopId"));
                orderMap.put(order.getOrderId(), order);
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Neuladen der Bestellungen", e);
        }
    }

    private static void reloadOrders() {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT orderId, orderTime," +
                    " weight, orderStatus, orderRouteStopId, locationId " +
                    "FROM orders " +
                    "ORDER BY orderId ASC");

            //Füge einzelne Bestellungen in DAO/Map ein
            while (resultSet.next()) {
                // If order is not in the ordermap, create new order
                if (!orderMap.containsKey(resultSet.getInt("orderId"))) {
                    Order order = new Order(
                            resultSet.getInt("orderId")
                            , new DateTime(resultSet.getTimestamp("orderTime"))
                            , LocationDAO.getLocation(resultSet.getInt("locationId"))
                            , resultSet.getInt("weight")
                            , resultSet.getInt("orderStatus")
                            , resultSet.getInt("orderRouteStopId"));
                    orderMap.put(order.getOrderId(), order);
                }
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Neuladen der Bestellungen", e);
        }
    }


    public static int countOrders() {
        int amountOfOrders = 0;
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(*) AS count " +
                    "FROM orders ");

            // Only one line available
            if (resultSet.first()) {
                amountOfOrders = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Zählen der Bestellungen", e);
        }
        return amountOfOrders;
    }

    public static int countOrdersPerDay() {
        int amountOfOrders = 0;
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CAST(AVG(count) AS INTEGER) AS average " +
                    "FROM (SELECT COUNT(*) AS count FROM orders GROUP BY DATE(orderTime)) tbl");

            // Only one line available
            if (resultSet.first()) {
                amountOfOrders = resultSet.getInt("average");
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Zählen der Bestellungen", e);
        }
        return amountOfOrders;
    }

    public static int averageWeight() {
        int averageWeight = 0;
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CAST(AVG(weight) AS INTEGER) AS average FROM orders");

            // Only one line available
            if (resultSet.first()) {
                averageWeight = resultSet.getInt("average");
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Berechnen des Durchschnitts", e);
        }
        return averageWeight;
    }

    public static Map<Date, Integer> ordersPerDay() {
        return null;
    }

    /**
     * Gebe Bestellung mit angegebener OrderId zurück
     *
     * @param orderId Bestellnummer
     * @return Bestellung mit angegebener Bestellnummer
     */
    public static Order getOrder(Integer orderId) {
        return orderMap.get(orderId);
    }

    /**
     * Füge neue Bestellung in DAO/Map ein
     *
     * @param order Bestellung, welche eingefügt werden soll.
     * @return Bestellung als DAO Element
     */
    public static Order addOrder(Order order) {
        try {
            String sqlStatement;
            PreparedStatement preparedStatement;

            Location _location = order.getLocation();
            if (_location != null && _location.locationId == 0) {
                if (_location.longitude == 0 && _location.latitude == 0) {
                    if (_location.getName() != null) {
                        order.setLocation(LocationDAO.getLocation(_location.getName()));
                    }
                }
            }
            sqlStatement = "INSERT INTO orders (orderTime, locationId" +
                    ", weight, orderStatus, orderRouteStopId) VALUES (?,?,?,?,?)";
            preparedStatement = dbConnection.prepareStatement(
                    sqlStatement, Statement.RETURN_GENERATED_KEYS);

            if (order.getOrderTime() != null)
                preparedStatement.setTimestamp(1, new Timestamp(order.getOrderTime().getMillis()));
            else
                preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            preparedStatement.setInt(2, order.getLocation().locationId);

            preparedStatement.setInt(3, order.getWeight());

            if (order.getStatus() != null)
                preparedStatement.setInt(4, order.getStatus().GetID());
            else
                preparedStatement.setInt(4, 0);

            preparedStatement.setInt(5, order.getRouteStopId());

            preparedStatement.execute();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Hinzufügen einer Bestellung", e);
        }

        //Füge Drohne in DAO ein
        orderMap.put(order.getOrderId(), order);

        return order;
    }

    /**
     * Aktualisiere bestehende Bestellung
     *
     * @param order zu aktualisierende Bestellung
     * @return Aktualisierte Bestellung
     */
    public static Order updateOrder(Order order) {

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
                    "UPDATE orders " +
                            "SET locationId = ?,orderStatus=?,orderRouteStopId=?,weight=? " +
                            "WHERE orderId = ?");
            if (order.getLocation() != null)
                preparedStatement.setInt(1, order.getLocation().locationId);
            else
                preparedStatement.setInt(1, 0);
            if (order.getStatus() != null)
                preparedStatement.setInt(2, order.getStatus().GetID());
            else
                preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, order.getRouteStopId());
            preparedStatement.setInt(4, order.getWeight());
            preparedStatement.setInt(5, order.getOrderId());
            preparedStatement.execute();

            orderMap.put(order.getOrderId(), order);

        } catch (SQLException e) {
            _log.error("Fehler beim Aktualisieren einer Bestellung", e);
        }


        return order;
    }

    /**
     * Lösche Bestellung mit angegebener Bestellnummer
     *
     * @param orderId Bestellnummer
     */
    public static void deleteOrder(Integer orderId) {
        //TODO Lösche Drohne in DB
        orderMap.remove(orderId);
    }

    /**
     * Lade alle bestehenden Bestellungen
     *
     * @return List<Order> mit allen Bestellungen
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
        orderCollection.forEach((Order order) -> {
            if (order.getStatus() == _status) {
                list.add(order);
            }
        });
        return list;
    }

    /**
     * Lade alle Bestellungen, welche die angegebene Routenstopnummer haben
     *
     * @param _routeStopId Stoppunkt auf der Route der Drohne
     * @return Bestellungen mit angegebener RouteStopId
     */
    public static List<Order> getOrdersWithRouteStopId(int _routeStopId) {
        Collection<Order> orderCollection = orderMap.values();
        List<Order> list = new ArrayList<>();
        orderCollection.forEach((Order order) -> {
            if (order.getRouteStopId() == _routeStopId) {
                list.add(order);
            }
        });
        return list;
    }
}