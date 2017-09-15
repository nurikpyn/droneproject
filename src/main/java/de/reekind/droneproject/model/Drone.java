package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import de.reekind.droneproject.model.enumeration.DroneStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Timer;
import java.util.TimerTask;

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

    public void StartDrone(int routeId) {
        //Setze Drohnenstatus
        //Location berechnen falls gefragt
        //Wenn zeit von start dauer bis zum abladen: bestellstatus ändern
        //5 min vor Ende calculator anwerfen
        int MINUTES = 10; // The delay in minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() { // Function runs every MINUTES minutes.
                // Run the code you want here - has to be static
            }
        }, 0, 1000 * 60 * MINUTES);
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

    public void updateDroneStatus(DroneStatus droneStatus)
    {
        if (droneStatus == DroneStatus.GetValue(0))
        {
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

    public VehicleImpl toJspritVehicle() {
        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance(Integer.toString(droneId));
        vehicleBuilder.setStartLocation(depot.getLocation().toJspritLocation());
        vehicleBuilder.setType(droneType.toJspritVehicleType());
        return vehicleBuilder.build();
    }
}
