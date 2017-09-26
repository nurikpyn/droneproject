package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;
import de.reekind.droneproject.model.routeplanning.RouteCalculator;
import org.joda.time.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Timer;
import java.util.TimerTask;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {

    private int droneId;
    private DroneType droneType;
    private DroneStatus droneStatus = DroneStatus.Bereit;
    private Double latitude;
    private Double longitude;
    private Depot depot;
    private float speed = 0;
    private int uptime = 0;

    public Drone() {
    }

    public Drone(int _droneId, DroneType _droneType, int _droneStatus, Depot _droneDepot) {
        this.droneId = _droneId;
        this.droneType = _droneType;
        this.droneStatus = DroneStatus.GetValue(_droneStatus);
        this.depot = _droneDepot;
        if (this.depot != null) {
            this.latitude = depot.getLatitude();
            this.longitude = depot.getLongitude();
        }
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

        if (droneStatus == DroneStatus.Bereit) {
            speed = 0;
            uptime = 0;

            RouteCalculator routeCalc = new RouteCalculator();
            routeCalc.calculateRoute();
        }
        if (droneStatus == DroneStatus.InAuslieferung) {
            speed = 60;
            uptime = 10;
        }

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
        if (this.depot != null) {
            this.latitude = depot.getLatitude();
            this.longitude = depot.getLongitude();
        }
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public VehicleImpl toJspritVehicle() {
        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance(Integer.toString(droneId));
        vehicleBuilder.setStartLocation( com.graphhopper.jsprit.core.problem.Location.newInstance(this.latitude, this.longitude));
        vehicleBuilder.setType(droneType.toJspritVehicleType());
        return vehicleBuilder.build();
    }
}
