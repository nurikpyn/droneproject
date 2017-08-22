package de.reekind.droneproject.model;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Date;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private int orderId;
    private Timestamp orderTime;
    private int weight;
    private int orderStatus;
    private int droneId;
    private Location location;

    public Order() {}
 public Order(int _orderId, Timestamp _orderTime, double adressLatitude, double adressLongitude, int _weight, int _orderStatus)
 {
    this.orderId = _orderId;
    this.orderTime = _orderTime;
     this.location = new Location(adressLatitude, adressLongitude);
    this.weight = _weight;
    this.orderStatus = _orderStatus;
    // -1 to signify NOT SET
    this.droneId = -1;
 }

 //neue Order
 public Order(int orderID, Timestamp orderTime, int orderStatus, String adress, int weight) {
        this.orderId = orderID;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.weight = weight;
     //Get ID
     //Map Adress
     try {
         GeoApiContext context = new GeoApiContext.Builder()
                 .apiKey("AIzaSyBgKJti8sqVUbrvQY2xWVKgXX4WF_Y4npE")
                 .build();
         GeocodingResult[] results =  GeocodingApi.geocode(context,adress).await();
         this.location = new Location(results[0].geometry.location.lat,
                 results[0].geometry.location.lng, adress);
     } catch (Exception ex){
         System.out.println(ex.getMessage());
     }
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

    public int getStatus() {
        return orderStatus;
    }

    public void setStatus(int orderStatus) {
        this.orderStatus = orderStatus;
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
