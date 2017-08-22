package de.reekind.droneproject.model;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ownLocation")
public class Location {
    public Location() {}
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Location(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double latitude;
    public double longitude;
    public String name;

    public com.graphhopper.jsprit.core.problem.Location toJspritLocation() {
        return com.graphhopper.jsprit.core.problem.Location.newInstance(this.latitude,this.longitude);
    }
}
