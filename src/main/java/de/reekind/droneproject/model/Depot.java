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
    private Location location;

    public Depot() {
    }
    public Depot(int depotID, Location location) {
        this.depotID = depotID;
        this.location = location;
    }

    public Depot(int depotID) {
        this.depotID = depotID;
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


}