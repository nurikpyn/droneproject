package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graphhopper.jsprit.core.problem.Location;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by timbe on 07.08.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonFormat(shape= JsonFormat.Shape.ARRAY)
public class Depot {
    private int depotID;

    public Depot(int depotID, Location location) {
        this.depotID = depotID;
        this.location = location;
    }
    public Depot(int depotID) {
        this.depotID = depotID;
    }

    private com.graphhopper.jsprit.core.problem.Location location;

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
        if (obj.toString().equals(Integer.toString(this.depotID))) {
            return true;
        } else {
            return super.equals(obj);
        }
    }
    @Override
    public String toString() {
        return Integer.toString(this.depotID);
    }
}