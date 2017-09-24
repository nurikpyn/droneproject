package de.reekind.droneproject.model.routeplanning;

import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import org.joda.time.DateTime;

import java.util.TimerTask;

public class DroneRestartTimer extends TimerTask
{

    private Drone drone;

    public DroneRestartTimer(Drone drone)
    {
        this.drone = drone;
    }

    @Override
    public void run()
    {
        drone.setDroneStatus(DroneStatus.Bereit);
    }
}
