package de.reekind.droneproject.domain;

import java.util.*;

public class Order {

    private int orderId;
    private Date orderTime;
    private int addressId;
    private float weight;
    private int status;
    private int droneId;

 Order(int _orderId, Date _orderTime, int _addressId, float _weight, int _status)
 {
    this.orderId = _orderId;
    this.orderTime = _orderTime;
    this.addressId = _addressId;
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

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
}
