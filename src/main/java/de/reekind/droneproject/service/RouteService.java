package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.RouteDAO;
import de.reekind.droneproject.model.routeplanning.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/routes")
public class RouteService {
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Route> getRoutes_JSON() {
        return RouteDAO.getAllRoutes();
    }

    @GET
    @Path("/{routeId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Route getRoute(@PathParam("routeId") Integer routeId) {
        return RouteDAO.getRoute(routeId);
    }
}
