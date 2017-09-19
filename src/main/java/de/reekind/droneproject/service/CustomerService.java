package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.OrderHistoryPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/public/customerservice")
public class CustomerService {
    @GET
    @Path("/orderinfo/{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<OrderHistoryPoint> getOrderInfo(@PathParam("orderId") Integer orderId) {
        return OrderDAO.getOrderHistory(orderId);
    }
}
