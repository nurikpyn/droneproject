package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.Location;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {

    private int droneId;
    private DroneType droneType;
    private int droneStatus;
    private Location droneLocation;
    private Depot droneDepot;

    public Drone(){}

    // New Constructor for use in RouteCalculator
    public Drone(int _droneId, DroneType _droneType, int _droneStatus, Depot _droneDepot)
    {
        this.droneId = _droneId;
        this.droneType = _droneType;
        this.droneStatus = _droneStatus;
        this.droneDepot = _droneDepot;
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
}
