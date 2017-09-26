package de.reekind.droneproject.model.task;

import de.reekind.droneproject.model.Drone;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import de.reekind.droneproject.model.routeplanning.RouteCalculator;

import java.util.TimerTask;

public class DroneTimer extends TimerTask
{

    private Drone drone;
    private DroneStatus droneStatus;

    public DroneTimer(Drone drone, DroneStatus droneStatus)
    {
        this.drone = drone;
        this.droneStatus = droneStatus;
        if (droneStatus == DroneStatus.Bereit) {
            RouteCalculator routeCalc = new RouteCalculator();
            routeCalc.calculateRoute();
        }
    }

    @Override
    public void run()
    {
        drone.setDroneStatus(droneStatus);
    }
}
