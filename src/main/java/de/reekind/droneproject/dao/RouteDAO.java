package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.Location;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private final static Logger _log = LogManager.getLogger();
    private static Connection dbConnection;

    static {
        dbConnection = DbUtil.getConnection();
    }

    /**
     * Auflisten aller Routen
     *
     * @return Liste mit allen Routen
     */
    public static List<Route> getAllRoutes() {
        _log.debug("Laden aller Routen");
        List<Route> list = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT routeID, droneId" +
                            ", startTime, endTime, routeStatus, totalDistance " +
                            "FROM routes");
            while (resultSet.next()) {
                _log.debug("Lade Routenstops für Route {}", resultSet.getInt("routeID"));
                PreparedStatement routeStopsStatement = dbConnection.prepareStatement("SELECT routeStopId, locationId, arrivalTime, routeDistanceTillStop" +
                        " FROM routestops WHERE routeId = ?");
                routeStopsStatement.setInt(1, resultSet.getInt("routeID"));
                ResultSet routeStopResults = routeStopsStatement.executeQuery();
                ArrayList<RouteStop> routeStops = new ArrayList<>();
                _log.debug("Anzahl Routenstops {}", routeStopResults.getFetchSize());
                while (routeStopResults.next()) {
                    ArrayList<Order> orders = (ArrayList<Order>) OrderDAO.getOrdersWithRouteStopId(routeStopResults.getInt("routeStopId"));

                    RouteStop stop = new RouteStop(routeStopResults.getInt("routeStopId")
                            , new DateTime(routeStopResults.getTimestamp("arrivalTime"))
                            , LocationDAO.getLocation(routeStopResults.getInt("locationId"))
                            , orders
                            , routeStopResults.getDouble("routeDistanceTillStop")
                    );
                    routeStops.add(stop);
                }

                Drone drone = DroneDAO.getDrone(resultSet.getInt("droneId"));

                Route route = new Route(resultSet.getInt("routeID")
                        , drone
                        , new DateTime(resultSet.getTimestamp("startTime"))
                        , new DateTime(resultSet.getTimestamp("endTime"))
                        , routeStops
                        , RouteStatus.GetValue(resultSet.getInt("routeStatus"))
                        , new Location(drone.getDepot().getLatitude(), drone.getDepot().getLongitude())
                        , resultSet.getDouble("totalDistance")
                );

                list.add(route);
            }
            _log.debug("Insgesamt {} Routen geladen", list.size());
        } catch (SQLException e) {
            _log.error("Fehler beim Auflisten der Routen", e);
        }
        return list;
    }

    public static List<Route> getAllRoutesWithStatus(RouteStatus routeStatus) {
        _log.debug("Laden aller Routen");
        List<Route> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT routeID, droneId" +
                    ", startTime, endTime, routeStatus, totalDistance " +
                    "FROM routes WHERE routeStatus = ?");
            preparedStatement.setInt(1,routeStatus.GetID());
            ResultSet resultSet = preparedStatement.executeQuery(
                    );
            while (resultSet.next()) {
                PreparedStatement routeStopsStatement = dbConnection.prepareStatement("SELECT routeStopId, locationId, arrivalTime, routeDistanceTillStop" +
                        " FROM routestops WHERE routeId = ?");
                routeStopsStatement.setInt(1, resultSet.getInt("routeID"));
                ResultSet routeStopResults = routeStopsStatement.executeQuery();
                ArrayList<RouteStop> routeStops = new ArrayList<>();
                while (routeStopResults.next()) {
                    ArrayList<Order> orders = (ArrayList<Order>) OrderDAO.getOrdersWithRouteStopId(routeStopResults.getInt("routeStopId"));

                    RouteStop stop = new RouteStop(routeStopResults.getInt("routeStopId")
                            , new DateTime(routeStopResults.getTimestamp("arrivalTime"))
                            , LocationDAO.getLocation(routeStopResults.getInt("locationId"))
                            , orders
                            , routeStopResults.getDouble("routeDistanceTillStop")
                    );
                    routeStops.add(stop);
                }

                Drone drone = DroneDAO.getDrone(resultSet.getInt("droneId"));
                Route route = new Route(resultSet.getInt("routeID")
                    , drone
                    , new DateTime(resultSet.getTimestamp("startTime"))
                    , new DateTime(resultSet.getTimestamp("endTime"))
                    , routeStops
                    , RouteStatus.GetValue(resultSet.getInt("routeStatus"))
                , new Location(drone.getDepot().getLatitude(), drone.getDepot().getLongitude())
                        , resultSet.getDouble("totalDistance"));

                list.add(route);
            }
            _log.debug("Insgesamt {} Routen geladen", list.size());
        } catch (SQLException e) {
            _log.error("Fehler beim Auflisten der Routen", e);
        }
        return list;
    }

    /**
     * Auflisten einer bestimmten Route
     *
     * @param routeId Id der Route
     * @return Gewünschte Route
     */
    public static Route getRoute(Integer routeId) {
        Route route = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement(
                    "SELECT routeID, droneId, startTime, endTime, routeStatus, totalDistance " +
                            "FROM routes WHERE routeID = ?");
            statement.setInt(1, routeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PreparedStatement routeStopsStatement = dbConnection.prepareStatement(
                        "SELECT routeStopId, locationId, arrivalTime, routeDistanceTillStop" +
                                " FROM routestops WHERE routeId = ?");
                routeStopsStatement.setInt(1, routeId);
                ResultSet routeStopResults = routeStopsStatement.executeQuery();
                ArrayList<RouteStop> routeStops = new ArrayList<>();
                while (routeStopResults.next()) {
                    ArrayList<Order> orders = (ArrayList<Order>) OrderDAO
                            .getOrdersWithRouteStopId(routeStopResults.getInt("routeStopId"));

                    RouteStop stop = new RouteStop(routeStopResults.getInt("routeStopId")
                            , new DateTime(routeStopResults.getTimestamp("arrivalTime"))
                            , LocationDAO.getLocation(routeStopResults.getInt("locationId"))
                            , orders
                            , routeStopResults.getDouble("routeDistanceTillStop")
                    );
                    routeStops.add(stop);
                }
                Drone drone = DroneDAO.getDrone(resultSet.getInt("droneId"));
                route = new Route(resultSet.getInt("routeID")
                        , DroneDAO.getDrone(resultSet.getInt("droneId"))
                        , new DateTime(resultSet.getTimestamp("startTime"))
                        , new DateTime(resultSet.getTimestamp("endTime"))
                        , routeStops
                        , RouteStatus.GetValue(resultSet.getInt("routeStatus"))
                , new Location(drone.getDepot().getLatitude(), drone.getDepot().getLongitude())
                , resultSet.getDouble("totalDistance"));
            }
        } catch (SQLException e) {
            _log.error("Laden einer bestimmten Route", e);
        }
        return route;
    }

    /**
     * Hinzufügen von neuen Routen
     *
     * @param route Route, die hinzugefügt werden soll
     * @return Route mit RouteId
     */
    public static Route addRoute(Route route) {
        if (route != null) {
            try {
                PreparedStatement statement = dbConnection.prepareStatement(
                        "INSERT INTO routes (startTime, endTime, droneID, routeStatus, totalDistance)" +
                                " VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setTimestamp(1, new Timestamp(route.StartTime.getMillis()));
                statement.setTimestamp(2, new Timestamp(route.EndTime.getMillis()));
                statement.setInt(3, route.Drone.getDroneId());
                statement.setInt(4, route.getRouteStatus().GetID());
                statement.setDouble(5, route.TotalDistance);
                statement.execute();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        route.RouteId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Fehler beim Erstellen der Route. Kein Primärschlüssel erhalten.");
                    }
                }
                for (RouteStop routeStop : route.RouteStops) {
                    PreparedStatement routeStopsStatement = dbConnection.prepareStatement(
                            "INSERT INTO routestops (routeId, locationId, routeDistanceTillStop) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    routeStopsStatement.setInt(1, route.RouteId);
                    if (routeStop.Location != null)
                        routeStopsStatement.setInt(2, routeStop.Location.locationId);
                    else
                        routeStopsStatement.setInt(2, 0);
                    routeStopsStatement.setDouble(3, routeStop.RouteDistanceTillStop);
                    routeStopsStatement.execute();

                    //Speichere generierte Id des RouteStops
                    try (ResultSet generatedKeys = routeStopsStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            routeStop.setRouteStopId(generatedKeys.getInt(1));
                        } else {
                            throw new SQLException("Fehler beim Erstellen der RouteStops. Kein Primärschlüssel erhalten.");
                        }
                    }

                    //Speichere RouteStopId in Bestellungen
                    for (Order order : routeStop.Orders) {
                        order.setRouteStopId(routeStop.getRouteStopId());
                        OrderDAO.updateOrder(order);
                    }
                }
            } catch (SQLException e) {
                _log.error("Fehler beim Hinzufügen einer Route", e);
            }
        }
        return route;
    }

    /**
     * Aktualisieren einer bestehenden Route
     * @param route Aktualisierte Route
     * @return Aktualisierte Route im DAO
     */
    public static Route updateRoute(Route route) {
        if (route != null) {
            try {
                PreparedStatement statement = dbConnection.prepareStatement(
                        "UPDATE  routes SET startTime = ?, endTime = ?, droneID  = ?, routeStatus = ?, totalDistance = ? " +
                                " WHERE routeId = ?");
                statement.setTimestamp(1, new Timestamp(route.StartTime.getMillis()));
                statement.setTimestamp(2, new Timestamp(route.EndTime.getMillis()));
                statement.setInt(3, route.Drone.getDroneId());
                statement.setInt(4, route.getRouteStatus().GetID());
                statement.setDouble(5, route.TotalDistance);
                statement.setInt(6,route.RouteId);
                statement.execute();

                for (RouteStop routeStop : route.RouteStops) {
                    PreparedStatement routeStopsStatement = dbConnection.prepareStatement(
                            "UPDATE routestops SET routeId = ?, locationId = ?, routeDistanceTillStop = ? WHERE routeStopId = ?");
                    routeStopsStatement.setInt(1, route.RouteId);
                    if (routeStop.Location != null)
                        routeStopsStatement.setInt(2, routeStop.Location.locationId);
                    else
                        routeStopsStatement.setInt(2, 0);
                    routeStopsStatement.setDouble(3, routeStop.RouteDistanceTillStop);
                    routeStopsStatement.setInt(4,routeStop.getRouteStopId());
                    routeStopsStatement.execute();

                    //Speichere RouteStopId in Bestellungen
                    for (Order order : routeStop.Orders) {
                        order.setRouteStopId(routeStop.getRouteStopId());
                        OrderDAO.updateOrder(order);
                    }
                }
            } catch (SQLException e) {
                _log.error("Fehler beim Aktualisieren einer Route", e);
            }
        } else {
            _log.error("UpdateRoute : Route ist Null");
        }
        return route;
    }
}
