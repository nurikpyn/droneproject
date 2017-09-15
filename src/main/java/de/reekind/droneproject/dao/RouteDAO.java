package de.reekind.droneproject.dao;

import de.reekind.droneproject.DbUtil;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteStop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private static Connection dbConnection;
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
}
