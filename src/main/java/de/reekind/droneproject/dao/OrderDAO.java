package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.OrderHistoryPoint;
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

                if (order.validateOrder())
                    orderMap.put(order.getOrderId(), order);
                else
                    _log.error(String.format("Bestellung mit orderId %d ist nicht valide.",order.getOrderId()));
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

        //Validiere Bestellung
        if (order.validateOrder())
            orderMap.put(order.getOrderId(), order);
        else
            _log.error(String.format("Bestellung mit orderId %d ist nicht valide.",order.getOrderId()));

        try {
            String sqlStatement;
            PreparedStatement preparedStatement;

            Location _location = order.getLocation();
            //Existierende Location mit LocationID aus DB holen oder neue erstellen
            _location = LocationDAO.addLocation(_location);
            sqlStatement = "INSERT INTO orders (orderTime, locationId" +
                    ", weight, orderStatus, orderRouteStopId) VALUES (?,?,?,?,?)";
            preparedStatement = dbConnection.prepareStatement(
                    sqlStatement, Statement.RETURN_GENERATED_KEYS);

            if (order.getOrderTime() != null)
                preparedStatement.setTimestamp(1, new Timestamp(order.getOrderTime().getMillis()));
            else
                preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            preparedStatement.setInt(2, _location.locationId);

            preparedStatement.setInt(3, order.getWeight());

            if (order.getOrderStatus() != null)
                preparedStatement.setInt(4, order.getOrderStatus().GetID());
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
            if (order.getOrderStatus() != null)
                preparedStatement.setInt(2, order.getOrderStatus().GetID());
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
            if (order.getOrderStatus() == _status) {
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

    public static List<OrderHistoryPoint> getOrderHistory(int orderId) {
        List<OrderHistoryPoint> history = new ArrayList<>();

        OrderHistoryPoint point1 = new OrderHistoryPoint();
        point1.Caption = "Bestellung eingegangen";
        point1.Details = "Ihre Bestellung ist an unser System übermittelt worden und wartet nun auf die Abfertigung.";
        point1.OrderHistoryPointType = 0;
        OrderHistoryPoint point2 = new OrderHistoryPoint();
        point2.Caption = "Vorbereitung";
        point2.Details = "Ihre Bestellung wird für den Versand vorbereitet.";
        point2.OrderHistoryPointType = 1;
        OrderHistoryPoint point3 = new OrderHistoryPoint();
        point3.Caption = "In Zustellung";
        point3.Details = "Ihre Bestellung wird gerade zugestellt.";
        point3.OrderHistoryPointType = 2;
        OrderHistoryPoint point4 = new OrderHistoryPoint();
        point4.Caption = "Zugestellt";
        point4.Details = "Ihre Bestellung wurde erfolgreich zugestellt.";
        point4.OrderHistoryPointType = 3;
        history.add(point1);
        history.add(point2);
        history.add(point3);
        history.add(point4);
        return history;
    }
}