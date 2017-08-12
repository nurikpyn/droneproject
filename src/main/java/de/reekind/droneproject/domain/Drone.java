package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonFormat(shape= JsonFormat.Shape.ARRAY)

/**
 * Drone resource placeholder for json/xml representation
 */
public class Drone {

    @XmlElement(name = "id")
    private int droneId;
    @XmlElement(name = "typeid")
    private int droneTypeId;
    @XmlElement(name = "status")
    private int droneStatus;
    @XmlElement(name = "location")
    private Location location;
    /**
     * Blank constructor
     */
    public Drone() {
        //TODO get new ID
        //TODO create constructor for ID and Location
    }
    // New Constructor for use in RouteCalculator
    public Drone(int _droneId, int _droneTypeId, int _droneStatus)
    {
        this.droneId = _droneId;
        this.droneTypeId = _droneTypeId;
        this.droneStatus = _droneStatus;
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

    public int getDroneTypeId() {
        return droneTypeId;
    }

    public void setDroneTypeId(int droneTypeId) {
        this.droneTypeId = droneTypeId;
    }

    public int getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(int droneStatus) {
        this.droneStatus = droneStatus;
    }
}
