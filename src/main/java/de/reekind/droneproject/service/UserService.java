package de.reekind.droneproject.service;

import de.reekind.droneproject.dao.UserDAO;
import de.reekind.droneproject.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@Path("/public/authenticate")
public class UserService {
    private final static Logger _log = LogManager.getLogger();
    @POST
    public Response authenticate(User paramUser) {
        _log.debug("user {} password {}", paramUser.getUsername(), paramUser.getPassword());

        User user = UserDAO.getUser(paramUser.getUsername());
        _log.debug("user from DAO id {}user {} password {}", user.getUserId(), user.getUsername(), user.getPassword());
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
