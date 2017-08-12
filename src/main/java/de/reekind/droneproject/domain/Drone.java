package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonFormat(shape= JsonFormat.Shape.ARRAY)

/**
 * Drone resource placeholder for json/xml representation
 */
public class Drone {

    private int droneId;
    private int droneTypeId;
    private int droneStatus;
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

    public Drone(int ID) {
        this.id = ID;
    }
    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "location")
    private Location location;

    public int getID() {
        return id;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        //TODO Set Location in DB (only on shutdown?)
    }
}
