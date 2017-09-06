package de.reekind.droneproject.model;

import de.reekind.droneproject.model.enumeration.DroneStatus;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {

    private int droneId;
    private String droneName = "";
    private DroneType droneType;
    private DroneStatus droneStatus = DroneStatus.Bereit;
    private Location location;
    private Depot depot;
    private float speed = 60;
    private int uptime = 20;

    public Drone(){}

    // New Constructor for use in RouteCalculator
    public Drone(int _droneId, String _droneName, DroneType _droneType, int _droneStatus, Depot _droneDepot)
    {
        this.droneId = _droneId;
        this.droneName = _droneName;
        this.droneType = _droneType;
        this.droneStatus = DroneStatus.GetValue(_droneStatus);
        this.depot = _droneDepot;
        if (this.depot != null)
            this.location = _droneDepot.getLocation();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        //TODO Set Location in DB (only on shutdown?)
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
    }


    public DroneStatus getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(DroneStatus droneStatus) {
        this.droneStatus = droneStatus;
    }

    public DroneType getDroneType() {
        return droneType;
    }

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }
}
