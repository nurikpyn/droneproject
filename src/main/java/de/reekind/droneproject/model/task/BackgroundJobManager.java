package de.reekind.droneproject.model.task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@WebListener
public class BackgroundJobManager implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //scheduler = Executors.newSingleThreadScheduledExecutor();
        // scheduler.scheduleAtFixedRate(new DailyJob(), 0, 1, TimeUnit.DAYS);
        //Führe Job jede Minute aus
        //scheduler.scheduleAtFixedRate(new LookForOrdersJob(), 0, 1, TimeUnit.MINUTES);
        //scheduler.scheduleAtFixedRate(new MinJob(), 0, 1, TimeUnit.MINUTES);
        // scheduler.scheduleAtFixedRate(new SecJob(), 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

}