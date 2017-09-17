package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Statistic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest/statistics")
public class StatisticsService {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        statistic.OrderCount = OrderDAO.countOrders();
        statistic.AverageWeight = OrderDAO.averageWeight();
        statistic.AverageOrdersPerDay = OrderDAO.countOrdersPerDay();
        return statistic;
    }
}
