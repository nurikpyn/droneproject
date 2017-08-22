package de.reekind.droneproject;

import de.reekind.droneproject.filter.CORSResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class RestDroneProjectApp extends ResourceConfig {
    public RestDroneProjectApp() {
        register(CORSResponseFilter.class);
    }
}
