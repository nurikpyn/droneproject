package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.OrderHistoryPoint;
import de.reekind.droneproject.model.OrderImport;
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
                    order.setOrderStatus(OrderStatus.Fehler);
                    OrderDAO.addOrderHistoryPoint(order);
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


    public static Order addOrderImport(OrderImport orderImport) {
        //Vorgelagert, Damit Location etc schon stimmen.
        Order order = new Order(orderImport.OrderTime, orderImport.LocationName, orderImport.Weight);
        return OrderDAO.addOrder(order);
    }

    /**
     * Füge neue Bestellung in DAO/Map ein
     *
     * @param order Bestellung, welche eingefügt werden soll.
     * @return Bestellung als DAO Element
     */
    public static Order addOrder(Order order) {

        //Validiere Bestellung
        if (!order.validateOrder())
            _log.error(String.format("Bestellung mit orderId %d ist nicht valide.",order.getOrderId()));

        try {
            String sqlStatement;
            PreparedStatement preparedStatement;

            //Existierende Location mit LocationID aus DB holen oder neue erstellen
            order.setLocation(LocationDAO.addLocation( order.getLocation()));
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

        //Füge Order in DAO ein
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
    public static boolean deleteOrder(Integer orderId) {
        if (orderMap.containsKey(orderId)) {
            //TODO Lösche Drohne in DB
            orderMap.remove(orderId);
            return true;
        }else {
            return false;
        }
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
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(
                    "SELECT orderHistoryId, orderId, caption, detail, statusId, timestamp " +
                            "FROM orderhistory WHERE orderId = ?");
            preparedStatement.setInt(1,orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                OrderHistoryPoint orderHistoryPoint = new OrderHistoryPoint(
                        resultSet.getInt("orderHistoryId")
                        , resultSet.getString("caption")
                        , resultSet.getString("detail")
                        , new DateTime(resultSet.getTimestamp("timestamp"))
                        , resultSet.getInt("statusId"));
                history.add(orderHistoryPoint);
            }

        } catch (SQLException e) {
           _log.error("Fehler beim Laden der Historie", e);
        }

        return history;
    }

        /**
     * Füge einen neuen Historieneintrag zur Bestelldetails hinzu
     * @param order Bestellung, die einen neuen Eintrag bekommt
     * @return Historieneintrag
     */
    public static OrderHistoryPoint addOrderHistoryPoint(Order order) {
        String caption = "";
        String details = "";
        switch (order.getOrderStatus()) {
            case Eingegangen:
                caption = "Bestellung eingegangen";
                details = "Ihre Bestellung ist an unser System übermittelt worden und wartet nun auf die Abfertigung.";
                break;
            case InVorbereitung:
                caption = "Vorbereitung";
                details = "Ihre Bestellung wird für den Versand vorbereitet.";
                break;
            case InAuslieferung:
                caption = "In Zustellung";
                details = "Ihre Bestellung wird gerade zugestellt.";
                break;
            case Ausgeliefert:
                caption = "Zugestellt";
                details = "Ihre Bestellung wurde an den gewünschten Lieferort zugestellt.";
                break;
            case Fehler:
                caption = "Fehler bei der Lieferung";
                details = "Leider gab es Komplikationen in Verbindung mit Ihrer Bestellung. Für Details wenden Sie sich bitte an Ihren Kundenbetreuer.";
                break;
            default:
                break;
        }
        OrderHistoryPoint point = new OrderHistoryPoint(caption, details,order.getOrderStatus().GetID() );

        try {
            PreparedStatement preparedStatement;
            preparedStatement = dbConnection.prepareStatement(
                    "INSERT INTO orderhistory (orderId, caption, detail, statusId) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setString(2,caption);
            preparedStatement.setString(3,details);
            preparedStatement.setInt(4, order.getOrderStatus().GetID());

            preparedStatement.execute();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                   point.OrderHistoryPointId =generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            _log.error("Fehler beim Hinzufügen eines Status", e);
        }
        return point;
    }
}