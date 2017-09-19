package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.OrderImport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Order addOrder(OrderImport order) {
        return OrderDAO.addOrderImport(order);
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Order updateOrder(Order order) {
        return OrderDAO.updateOrder(order);
    }

    @DELETE
    @Path("/{orderId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteOrder(@PathParam("orderId") Integer orderId) {
        if (OrderDAO.deleteOrder(orderId))
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

}