package de.reekind.droneproject.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.reekind.droneproject.domain.Drone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/drones")
public class DroneResource {
    public DroneResource(){
    }
    @JsonProperty("drone")
    private ArrayList<Drone> drones = new ArrayList<Drone>();

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Drone> getDrones()
            throws IOException {
        return drones;
    }

}
