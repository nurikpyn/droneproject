package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.DroneTypeDAO;
import de.reekind.droneproject.model.DroneType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/dronetypes")
public class DroneTypeService {
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<DroneType> getDroneTypes_JSON() {
        return DroneTypeDAO.getAllDroneTypes();
    }
    @GET
    @Path("/{droneTypeId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DroneType getDroneType(@PathParam("droneTypeId") Integer droneTypeId) {
        return DroneTypeDAO.getDroneType(droneTypeId);
    }

}
