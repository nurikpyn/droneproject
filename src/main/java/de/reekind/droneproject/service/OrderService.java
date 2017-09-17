package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/rest/orders")
public class OrderService {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Order> getOrders_JSON() {
        return OrderDAO.getAllOrders();
    }

    @GET
    @Path("/{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Order getOrder(@PathParam("orderId") Integer orderId) {
        return OrderDAO.getOrder(orderId);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Order addOrder(Order order) {
        return OrderDAO.addOrder(order);
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Order updateOrder(Order order) {
        return OrderDAO.updateOrder(order);
    }

    @DELETE
    @Path("/{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void deleteOrder(@PathParam("orderId") Integer orderId) {
        OrderDAO.deleteOrder(orderId);
    }

}