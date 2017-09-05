package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.DepotDAO;
import de.reekind.droneproject.model.Depot;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/depots")
public class DepotService {
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Depot> getDroneTypes_JSON() {
        return DepotDAO.getAllDepots();
    }
    @GET
    @Path("/{depotId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Depot getDroneType(@PathParam("depotId") Integer depotId) {
        return DepotDAO.getDepot(depotId);
    }

}
