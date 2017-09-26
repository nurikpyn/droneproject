package de.reekind.droneproject.model;


import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderHistoryPoint {
    public String Details;
    public String Caption;
    public int OrderHistoryPointType;
    public DateTime Timestamp;

    public OrderHistoryPoint() {
        this.Timestamp = DateTime.now();
    }

    public OrderHistoryPoint(String caption, String details, int orderHistoryPointType) {
        this.Timestamp = DateTime.now();
        this.Caption = caption;
        this.Details = details;
        this.OrderHistoryPointType = orderHistoryPointType;
    }

    public OrderHistoryPoint(String caption, String details, DateTime timestamp, int orderHistoryPointType) {
        this.Timestamp = timestamp;
        this.Caption = caption;
        this.Details = details;
        this.OrderHistoryPointType = orderHistoryPointType;
    }
}
