package de.reekind.droneproject.model.task;

import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.enumeration.DroneStatus;

import java.util.TimerTask;

public class DroneTimer extends TimerTask
{

    private Drone drone;
    private DroneStatus droneStatus;

    public DroneTimer(Drone drone, DroneStatus droneStatus)
    {
        this.drone = drone;
        this.droneStatus = droneStatus;
    }

    @Override
    public void run()
    {
        drone.setDroneStatus(droneStatus);
    }
}
