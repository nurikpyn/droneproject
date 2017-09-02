package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.UserDAO;
import de.reekind.droneproject.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/users")
public class UserService {

    @Path("/authenticate")
    @POST
    public Response authenticate(@FormParam("username") String username, @FormParam("password") String password) {
        if (username.equals("test") && password.equals("test"))
            return Response.ok().build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    // URI:
    // /contextPath/servletPath/users
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<User> getUsers_JSON() {
        return UserDAO.getAllUsers();
    }

    // URI:
    // /contextPath/servletPath/users/{userId}
    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getUser(@PathParam("userId") Integer userId) {
        return UserDAO.getUser(userId);
    }
}
