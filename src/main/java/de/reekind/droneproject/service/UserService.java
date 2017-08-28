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
    @GET
    public Response authenticate() {
        return Response.ok().build();
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

    // URI:
    // /contextPath/servletPath/users
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User addUser(User user) {
        return UserDAO.addUser(user);
    }

    // URI:
    // /contextPath/servletPath/users
    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User updateUser(User user) {
        return UserDAO.updateUser(user);
    }

    @DELETE
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void deleteUser(@PathParam("userId") Integer userId) {
        UserDAO.deleteUser(userId);
    }

}
