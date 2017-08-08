package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.maven.plugins.annotations.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Service class that handles REST requests
 */
@Path("/drones")
public class DroneResource {
    @JsonProperty("drone")
    private ArrayList<Drone> drones = new ArrayList<Drone>();


/*    *//**
     * Adds a new resource (podcast) from the given json format (at least title
     * and feed elements are required at the DB level)
     *
     * @param podcast
     * @return
     * @throws AppException
     *//*
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response createPodcast(Drone podcast) throws AppException {

        Long droneID = podcastService.createPodcast(podcast);
        return Response.status(Response.Status.CREATED)// 201
                .entity("A new drone has been created")
                .header("Location",
                        "http://localhost:8888/demo-rest-jersey-spring/podcasts/"
                                + String.valueOf(droneID)).build();
    }*/

    	/*
	 * *********************************** READ ***********************************
	 */
    /**
     * Returns all resources (podcasts) from the database
     *
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @GET
    //@Compress //can be used only if you want to SELECTIVELY enable compression at the method level. By using the EncodingFilter everything is compressed now.
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Drone> getDrones()
            throws IOException {
        Drone xx = new Drone(1);
        drones.add(xx);
        return drones;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public void addDrone(Drone drone) {
        this.drones.add(drone);
    }
}
