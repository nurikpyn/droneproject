package de.reekind.droneproject.resources;

/**
 * Created by timbe on 03.08.2017.
 */
import javax.ws.rs.*;

@Path("/orders")
public class OrderResource {

    @GET
    @Produces("text/plain")
    public int getOrdernumber() {
        return ordernumber;
    }

    protected int ordernumber = 1;

}