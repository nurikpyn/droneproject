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
    private double latitude;
    private double longitude;

    public Depot() {
    }
    public Depot(int _depotID, String _depotName, double _latitude, double _longitude) {
        this.depotID = _depotID;
        this.depotName = _depotName;
        this.latitude = _latitude;
        this.longitude = _longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}