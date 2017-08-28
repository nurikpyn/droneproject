package de.reekind.droneproject.model;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private int orderId;
    private Timestamp orderTime;
    private Timestamp orderReadyTime;
    private int weight;
    private OrderStatus orderStatus;
    private int droneId;
    private Location location;

    public Order() {}
    public Order(int _orderId, Timestamp _orderTime, double adressLatitude, double adressLongitude, int _weight, int _orderStatus)
    {
        this.orderId = _orderId;
        this.orderTime = _orderTime;
        this.location = new Location(adressLatitude, adressLongitude);
        this.weight = _weight;
        this.orderStatus = OrderStatus.values() [_orderStatus];
        // -1 to signify NOT SET
        this.droneId = -1;
    }

    //neue Order
    public Order(int orderID, Timestamp orderTime, int _orderStatus, String adress, int weight) {
        this.orderId = orderID;
        this.orderStatus =  OrderStatus.values() [_orderStatus];
        this.orderTime = orderTime;
        this.weight = weight;
        this.location = new Location(adress);
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public OrderStatus getStatus() {
        return orderStatus;
    }

    public void setStatus(int orderStatus) {
        this.orderStatus = OrderStatus.values() [orderStatus];
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

    public Timestamp getOrderReadyTime() {
        //TODO Make Preperation Time dynamic
        return new Timestamp(orderTime.getTime() + TimeUnit.MINUTES.toMillis(5));
    }
}
