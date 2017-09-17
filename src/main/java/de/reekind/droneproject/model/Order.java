package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.job.Service;
import de.reekind.droneproject.model.enumeration.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Duration;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    final static Logger _log = LogManager.getLogger();
    private int orderId;
    private DateTime orderTime;
    private DateTime orderReadyTime;
    private int weight;
    private int routeStopId;
    private OrderStatus orderStatus;
    private Location location;
    private List<OrderHistoryPoint> orderHistoryPointList;

    public Order() {
        if (!validateOrder()) {
            _log.error("Validierung der Bestellung fehlgeschlagen");
        }
    }


    public Order(DateTime _orderTime, String deliveryPlace, int weight) {
        this.orderTime = _orderTime;
        this.location = new Location(deliveryPlace);
        this.weight = weight;
        if (!validateOrder()) {
            _log.error("Validierung der Bestellung fehlgeschlagen");
        }
    }

    public Order(DateTime _orderTime, Location deliveryPlace, int weight) {
        this.orderTime = _orderTime;
        this.location = deliveryPlace;
        this.weight = weight;
        if (!validateOrder()) {
            _log.error("Validierung der Bestellung fehlgeschlagen");
        }
    }

    public Order(int _orderId, DateTime _orderTime, Location _location, int _weight, int _orderStatus, int _routeStopId) {
        this.orderId = _orderId;
        this.orderTime = _orderTime;
        this.location = _location;
        this.weight = _weight;
        this.orderStatus = OrderStatus.values()[_orderStatus];
        this.routeStopId = _routeStopId;
        if (!validateOrder()) {
            _log.error("Validierung der Bestellung fehlgeschlagen");
        }
    }

    //TODO Validierung der Bestellungen: Zeitpunkt nicht vor 2017, Adresse irgendwie im Raum, Gewicht unter 4000
    private boolean validateOrder() {
        return orderTime.isBeforeNow() && weight < 4001;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public DateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(DateTime orderTime) {
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
        this.orderStatus = OrderStatus.values()[orderStatus];
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DateTime getOrderReadyTime() {
        //TODO Make Preperation Time dynamic
        return orderTime.plusMinutes(5);
    }


    public Service toJspritService() throws IllegalArgumentException {
        Service.Builder servBuilder = Service.Builder.newInstance(Integer.toString(orderId));
        servBuilder.addSizeDimension(DroneType.WEIGHT_INDEX, weight);
        servBuilder.addSizeDimension(DroneType.PACKAGE_INDEX, 1);
        if (location != null)
            servBuilder.setLocation(location.toJspritLocation());
        else {
            throw new IllegalArgumentException(String.format("Tried to create Jsprit service with empty location. Order %d", this.orderId));
        }
        // Get time difference between order and now. Used for priority!
        long differenceDuration = orderTime.getMillis() - DateTime.now().getMillis();
        if (differenceDuration > Duration.ofHours(1).toMillis()) {
            // priority values from 1 to 10 are allowed where 1 = high and 10 is low
            // Orders older than one hour have to be sent ASAP.
            servBuilder.setPriority(1);
        } else {
            servBuilder.setPriority(2);
        }
        return servBuilder.build();
    }

    public int getRouteStopId() {
        return routeStopId;
    }

    public void setRouteStopId(int routeStopId) {
        this.routeStopId = routeStopId;
    }
}
