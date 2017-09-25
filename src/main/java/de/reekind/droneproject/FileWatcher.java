package de.reekind.droneproject;

import de.reekind.droneproject.model.OrderImport;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.joda.time.DateTime;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {
    public static void main(String[] args) {
        Path path = Paths.get("F:\\timbe\\Desktop1");
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
            WatchKey watchKey;
            while (true) {
                watchKey = watchService.poll(10, TimeUnit.MINUTES);
                if(watchKey != null) {
                    watchKey.pollEvents().forEach(event -> {
                        // Context for directory entry event is the file name of entry
                        @SuppressWarnings("unchecked")
                        Path name = ((WatchEvent<Path>)event).context();
                        Path child = path.resolve(name);

                        List<OrderImport> orders = readOrdersFromFile(child.toString());

                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            System.out.println(mapper.writeValueAsString(orders));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "testpw4admin#");
                        Client client = ClientBuilder.newClient();
                        client.register(feature);
                        WebTarget target = client.target("http://pphvs03.reekind.de:8080/rest").path("orders");

                        for(OrderImport order : orders) {
                            Invocation.Builder invocationBuilder =
                                    target.request(MediaType.APPLICATION_JSON);
                            invocationBuilder.header("accept", MediaType.APPLICATION_JSON);
                            Response response = invocationBuilder.post(Entity.json(order));
                            System.out.println(response.getStatus());
                            System.out.println(response.readEntity(String.class));
                        }
                    });
                }
                if (watchKey != null) {
                    watchKey.reset();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static List<OrderImport> readOrdersFromFile(String path) {
        String line = "";
        String cvsSplitBy = ";";

        List<OrderImport> orders = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] element = line.split(cvsSplitBy);

                orders.add(new OrderImport(DateTime.parse(element[0]), element[1],Integer.parseInt(element[2])));
            }
            return orders;

        } catch (IOException e) {
            e.printStackTrace();
            return orders;
        }
    }
}
