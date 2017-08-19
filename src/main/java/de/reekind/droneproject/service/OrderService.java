package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/orders")
public class OrderService {

    // URI:
    // /contextPath/servletPath/orders
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Order> getOrders_JSON() {
        return OrderDAO.getAllOrders();
    }

    // URI:
    // /contextPath/servletPath/orders/{orderId}
    @GET
    @Path("/{orderId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Order getOrder(@PathParam("orderId") Integer orderId) {
        return OrderDAO.getOrder(orderId);
    }

    // URI:
    // /contextPath/servletPath/orders
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Order addOrder(Order order) {
        return OrderDAO.addOrder(order);
    }

    // URI:
    // /contextPath/servletPath/orders
    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Order updateOrder(Order order) {
        return OrderDAO.updateOrder(order);
    }

    @DELETE
    @Path("/{orderId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void deleteOrder(@PathParam("orderId") Integer orderId) {
        OrderDAO.deleteOrder(orderId);
    }

}