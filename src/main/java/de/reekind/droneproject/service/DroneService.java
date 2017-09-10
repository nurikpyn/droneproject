package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.DroneDAO;
import de.reekind.droneproject.model.Drone;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/rest/drones")
public class DroneService {

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Drone> getDrones_JSON() {
        return DroneDAO.getAllDrones();
    }

    @GET
    @Path("/{droneId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Drone getDrone(@PathParam("droneId") Integer droneId) {
        return DroneDAO.getDrone(droneId);
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Drone addDrone(Drone drone) {
        return DroneDAO.addDrone(drone);
    }

    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Drone updateDrone(Drone drone) {
        return DroneDAO.updateDrone(drone);
    }

    @DELETE
    @Path("/{droneId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void deleteDrone(@PathParam("droneId") Integer droneId) {
        DroneDAO.deleteDrone(droneId);
    }

}