package de.reekind.droneproject.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderHistoryPoint {
    public int orderHistoryPointId;
    public String Details;
    public String Caption;
    public int OrderHistoryPointType;
    private Timestamp timestamp;

    public OrderHistoryPoint() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
