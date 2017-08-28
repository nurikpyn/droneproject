package de.reekind.droneproject.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {

    private int droneId;
    private String droneName;
    private DroneType droneType;
    private int droneStatus;
    private Location droneLocation;
    private Depot droneDepot;

    public Drone(){}

    // New Constructor for use in RouteCalculator
    public Drone(int _droneId, String _droneName, DroneType _droneType, int _droneStatus, Depot _droneDepot)
    {
        this.droneId = _droneId;
        this.droneName = _droneName;
        this.droneType = _droneType;
        this.droneStatus = _droneStatus;
        this.droneDepot = _droneDepot;
        this.droneLocation = new Location(5.5,5.33);
    }

    public Location getLocation() {
        return droneLocation;
    }

    public void setLocation(Location location) {
        this.droneLocation = location;
        //TODO Set Location in DB (only on shutdown?)
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
    }


    public int getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(int droneStatus) {
        this.droneStatus = droneStatus;
    }

    public DroneType getDroneType() {
        return droneType;
    }

    public void setDroneType(DroneType droneType) {
        this.droneType = droneType;
    }

    public Depot getDroneDepot() {
        return droneDepot;
    }

    public void setDroneDepot(Depot droneDepot) {
        this.droneDepot = droneDepot;
    }

    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }
}
