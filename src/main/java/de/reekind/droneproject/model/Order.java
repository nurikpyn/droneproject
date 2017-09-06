package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.job.Service;
import de.reekind.droneproject.model.enumeration.OrderStatus;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private int orderId;
    private Timestamp orderTime;
    private Timestamp orderReadyTime;
    private int weight;
    private OrderStatus orderStatus;
    private Drone drone;
    private Location location;
    private List<OrderHistoryPoint> orderHistoryPointList;

    //TODO Validierung der Bestellungen: Zeitpunkt nicht vor 2017, Adresse irgendwie im Raum, Gewicht unter 4000
    public Order() {
    }
    public Order(Timestamp _orderTime, String deliveryPlace, int weight) {
        this.orderTime = _orderTime;
        this.location = new Location(deliveryPlace);
        this.weight = weight;
    }
    public Order(Timestamp _orderTime, Location deliveryPlace, int weight) {
        this.orderTime = _orderTime;
        this.location = deliveryPlace;
        this.weight = weight;
    }
    public Order(int _orderId, Timestamp _orderTime, Location _location, int _weight, int _orderStatus)
    {
        this.orderId = _orderId;
        this.orderTime = _orderTime;
        this.location = _location;
        this.weight = _weight;
        this.orderStatus = OrderStatus.values() [_orderStatus];
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderTime() {
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

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Service toJspritService () {
        Service.Builder servBuilder = Service.Builder.newInstance(Integer.toString(orderId));
        servBuilder.addSizeDimension(DroneType.WEIGHT_INDEX, weight);
        servBuilder.addSizeDimension(DroneType.PACKAGE_INDEX, 1);
        servBuilder.setLocation(location.toJspritLocation());
        return servBuilder.build();
    }
}
