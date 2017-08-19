package de.reekind.droneproject.model;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ownLocation")
public class Location {
    public Location() {}
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double latitude;
    public double longitude;

    public com.graphhopper.jsprit.core.problem.Location toJspritLocation() {
        return com.graphhopper.jsprit.core.problem.Location.newInstance(this.latitude,this.longitude);
    }
}
