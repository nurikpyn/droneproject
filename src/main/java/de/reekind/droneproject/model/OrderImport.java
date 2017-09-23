package de.reekind.droneproject.model;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class OrderImport {
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    public DateTime OrderTime;
    public String LocationName;
    public int Weight;

    public OrderImport(){}

    public OrderImport(DateTime orderTime, String locationName, int weight) {
        this.OrderTime = orderTime;
        this.LocationName = locationName;
        this.Weight = weight;
    }
}
