package de.reekind.droneproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by timbe on 07.08.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Depot {
    private int depotID;
    private String depotName;
    private Location location;

    public Depot() {
    }

    public Depot(int _depotID, String _depotName, Location _location) {
        this.depotID = _depotID;
        this.depotName = _depotName;
        this.location = _location;
    }

    public Depot(int _depotID) {
        this.depotID = _depotID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDepotID() {
        return depotID;
    }

    public void setDepotID(int ID) {
        this.depotID = ID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(Integer.toString(this.depotID)) || super.equals(obj);
    }

    @Override
    public String toString() {
        return Integer.toString(this.depotID);
    }


    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }
}