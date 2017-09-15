package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private static Connection dbConnection;
    final static Logger _log = LogManager.getLogger();
    static {
        dbConnection = DbUtil.getConnection();
    }
    public static List<Route> getAllRoutes() {
        List<Route> list = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT routeID, droneId, startTime, endTime, routeStatus FROM routes");
            while(resultSet.next()) {

                PreparedStatement routeStopsStatement = dbConnection.prepareStatement("SELECT routeStopId, locationId, arrivalTime" +
                        " FROM routestops WHERE routeId = ?");
                routeStopsStatement.setInt(1,resultSet.getInt("routeID"));
                ResultSet routeStopResults = routeStopsStatement.executeQuery();
                ArrayList<RouteStop> routeStops = new ArrayList<>();
                while(routeStopResults.next()) {
                    RouteStop stop = new RouteStop(routeStopResults.getInt("routeStopId")
                            , routeStopResults.getTimestamp("arrivalTime").getTime()
                    , LocationDAO.getLocation(routeStopResults.getInt("locationId"))
                   );
                    routeStops.add(stop);
                }
                Route route = new Route(resultSet.getInt("routeID")
                        , DroneDAO.getDrone(resultSet.getInt("droneId"))
                ,  resultSet.getTimestamp("startTime").getTime()
                , resultSet.getTimestamp("endTime").getTime()
                , routeStops
                , RouteStatus.GetValue(resultSet.getInt("routeStatus")));

                list.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            _log.error(e);
        }
        return list;
    }

    public static Route getRoute(Integer routeId) {
        Route route = null;
        try {
            PreparedStatement statement = dbConnection.prepareStatement("SELECT routeID, droneId, startTime, endTime, routeStatus " +
                    "FROM routes WHERE routeID = ?");
            statement.setInt(1,routeId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                PreparedStatement routeStopsStatement = dbConnection.prepareStatement("SELECT routeStopId, locationId, arrivalTime" +
                        " FROM routestops WHERE routeId = ?");
                routeStopsStatement.setInt(1,routeId);
                ResultSet routeStopResults = routeStopsStatement.executeQuery();
                ArrayList<RouteStop> routeStops = new ArrayList<>();
                while(routeStopResults.next()) {
                    RouteStop stop = new RouteStop(routeStopResults.getInt("routeStopId")
                            , routeStopResults.getTimestamp("arrivalTime").getTime()
                            , LocationDAO.getLocation(routeStopResults.getInt("locationId"))
                    );
                    routeStops.add(stop);
                }

                route = new Route(resultSet.getInt("routeID")
                        , DroneDAO.getDrone(resultSet.getInt("droneId"))
                        , resultSet.getDouble("startTime")
                        , resultSet.getDouble("endTime")
                , routeStops
                , RouteStatus.GetValue(resultSet.getInt("routeStatus")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return route;
    }

    public static Route addRoute(Route route) {
        if(route != null) {
            try {
                PreparedStatement statement = dbConnection.prepareStatement(
                        "INSERT INTO routes (startTime, endTime, droneID, routeStatus) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setDouble(1,route.StartTime);
                statement.setDouble(2,route.EndTime);
                statement.setInt(3,route.Drone.getDroneId());
                statement.setInt(4,route.getRouteStatus().GetID());
                statement.execute();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        route.RouteId =  generatedKeys.getInt(1);
                    }
                    else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
                for (RouteStop routeStop : route.RouteStops){
                    PreparedStatement routeStopsStatement = dbConnection.prepareStatement(
                            "INSERT INTO routestops (routeId, locationId) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
                    routeStopsStatement.setInt(1,route.RouteId);
                    if (routeStop.Location != null)
                        routeStopsStatement.setInt(2,routeStop.Location.locationId);
                    else
                        routeStopsStatement.setInt(2,0);
                    routeStopsStatement.execute();

                    try (ResultSet generatedKeys = routeStopsStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            routeStop.setRouteStopId(generatedKeys.getInt(1));
                        }
                        else {
                            throw new SQLException("Creating order failed, no ID obtained.");
                        }
                    }
                }
            }
            catch (SQLException ex) {
                _log.error(ex);
            }
        }
        return route;
    }
}
