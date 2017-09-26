package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.UserDAO;
import de.reekind.droneproject.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/public/authenticate")
public class UserService {
    private final static Logger _log = LogManager.getLogger();

    @POST
    public Response authenticate(User paramUser) {
        User user = UserDAO.getUser(paramUser.getUsername());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            if (user.getUsername().equals(paramUser.getUsername()) && user.getPassword().equals(paramUser.getPassword()))
                return Response.ok().build();
            else
                return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
