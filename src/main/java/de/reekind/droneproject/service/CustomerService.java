package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/public/customerservice")
public class CustomerService {
    @GET
    @Path("/orderinfo/{orderId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String getOrderInfo(@PathParam("orderId") Integer orderId) {
        Order order = OrderDAO.getOrder(orderId);
        if (order != null && order.getStatus() != null)
            return order.getStatus().toString();
        else
            return "Bestellnummer ist nicht bekannt.";
    }
}
