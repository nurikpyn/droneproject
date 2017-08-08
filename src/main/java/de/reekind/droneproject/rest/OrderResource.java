package de.reekind.droneproject.rest;

/**
 * Created by timbe on 03.08.2017.
 */
import javax.ws.rs.*;

@Path("orders")
public class OrderResource {

    @GET
    @Produces("text/plain")
    public int getOrdernumber() {
        return ordernumber;
    }

    @POST
    @Consumes("text/plain")
    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    protected int ordernumber = 1;

}