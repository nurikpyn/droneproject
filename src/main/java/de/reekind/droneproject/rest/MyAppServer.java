package de.reekind.droneproject.rest;

import com.sun.net.httpserver.HttpServer;
import de.reekind.droneproject.domain.DroneResource;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by timbe on 03.08.2017.
 */
public class MyAppServer {
    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        ResourceConfig config = new ResourceConfig(DroneResource.class);
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

}
