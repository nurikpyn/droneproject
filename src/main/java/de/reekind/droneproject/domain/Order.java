package de.reekind.droneproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.graphhopper.jsprit.core.problem.Location;

import org.glassfish.jersey.client.ClientRequest;


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
    private com.graphhopper.jsprit.core.problem.Location location;

 Order(int _orderId, Timestamp _orderTime, double adressLatitude, double adressLongitude, float _weight, int _status)
 {
    this.orderId = _orderId;
    this.orderTime = _orderTime;
     this.location = Location.newInstance(adressLatitude, adressLongitude);
    this.weight = _weight;
    this.status = _status;
    // -1 to signify NOT SET
    this.droneId = -1;
 }

 //neue Order
 public Order(String adress, double weight) {
     //Get ID
     //Map Adress
     try {
         GeoApiContext context = new GeoApiContext.Builder()
                 .apiKey("AIzaSyBgKJti8sqVUbrvQY2xWVKgXX4WF_Y4npE")
                 .build();
         GeocodingResult[] results =  GeocodingApi.geocode(context,adress).await();
         this.location = Location.newInstance(results[0].geometry.location.lat,
                 results[0].geometry.location.lng);
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
