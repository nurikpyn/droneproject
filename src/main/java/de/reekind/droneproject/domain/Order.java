package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.*;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonFormat(shape= JsonFormat.Shape.ARRAY)
public class Order {

    @XmlElement(name = "orderid")
    private int orderId;
    @XmlElement(name = "ordertime")
    private Timestamp orderTime;
    @XmlElement(name = "weight")
    private float weight;
    @XmlElement(name = "status")
    private int status;
    @XmlElement(name = "droneid")
    private int droneId;
    @XmlElement(name = "location")
    private Location location;

 Order(int _orderId, Timestamp _orderTime, double adressLatitude, double adressLongitude, float _weight, int _status)
 {
    this.orderId = _orderId;
    this.orderTime = _orderTime;
     this.location = new Location(adressLatitude, adressLongitude);
    this.weight = _weight;
    this.status = _status;
    // -1 to signify NOT SET
    this.droneId = -1;
 }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
