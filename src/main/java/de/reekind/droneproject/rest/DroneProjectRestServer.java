package de.reekind.droneproject.rest;

import de.reekind.droneproject.ProblemSolverHelper;
import de.reekind.droneproject.domain.RouteCalculator;
import de.reekind.droneproject.resources.DroneResource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//Defines the base URI for all resource URIs.
@ApplicationPath("/")
public class DroneProjectRestServer extends Application {
    //The method returns a non-empty collection with classes, that must be included in the published JAX-RS application
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add( RouteCalculator.class );
        return h;
    }
}
