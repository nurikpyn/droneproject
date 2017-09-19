package de.reekind.droneproject.model;


import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderHistoryPoint {
    public int orderHistoryPointId;
    public String Details;
    public String Caption;
    public int OrderHistoryPointType;
    public DateTime timestamp;

    public OrderHistoryPoint() {
        this.timestamp = DateTime.now();
    }
}
