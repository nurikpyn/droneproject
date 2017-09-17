package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import de.reekind.droneproject.model.enumeration.DroneStatus;
import de.reekind.droneproject.model.enumeration.RouteStatus;
import de.reekind.droneproject.model.routeplanning.Route;
import org.joda.time.DateTime;

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
    private float speed = 60;
    private int uptime = 20;

    public Drone() {
    }

    public Drone(int _droneId, DroneType _droneType, int _droneStatus, Depot _droneDepot) {
        this.droneId = _droneId;
        this.droneType = _droneType;
        this.droneStatus = DroneStatus.GetValue(_droneStatus);
        this.depot = _droneDepot;
        if (this.depot != null) {
            this.latitude = depot.getLocation().latitude;
            this.longitude = depot.getLocation().longitude;
        }
    }

    public void StartDrone(Route route) {
        //Setze Drohnenstatus
        //Location berechnen falls gefragt
        //Wenn zeit von start dauer bis zum abladen: bestellstatus ändern
        //5 min vor Ende calculator anwerfen
        int MINUTES = 10; // The delay in minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // Function runs every MINUTES minutes.
                if (droneStatus == DroneStatus.InAuslieferung) {

                    if (new DateTime(route.EndTime).minusMinutes(5).equals(new DateTime())) {
                        droneStatus = DroneStatus.Bereit;
                        route.setRouteStatus(RouteStatus.Beendet);
                    }
                }
            }
        }, 0, 1000 * 60 * MINUTES);
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

        if (droneStatus == DroneStatus.GetValue(0)) {
            setDroneStatus(droneStatus);
            RouteCalculator routeCalc = new RouteCalculator();
            routeCalc.calculateRoute();
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
            this.latitude = depot.getLocation().latitude;
            this.longitude = depot.getLocation().longitude;
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
        vehicleBuilder.setStartLocation(depot.getLocation().toJspritLocation());
        vehicleBuilder.setType(droneType.toJspritVehicleType());
        return vehicleBuilder.build();
    }
}
