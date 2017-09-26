package de.reekind.droneproject.model;

import com.graphhopper.jsprit.core.problem.job.Service;
import de.reekind.droneproject.dao.DepotDAO;
import de.reekind.droneproject.dao.DroneTypeDAO;
import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.enumeration.OrderStatus;
import de.reekind.droneproject.model.task.OrderTimer;
import de.reekind.droneproject.model.task.RouteCalculatorTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Duration;
import java.util.Timer;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private final static Logger _log = LogManager.getLogger();
    private int orderId;
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    private DateTime orderTime;
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    private DateTime deliveryTime;
    private int weight;
    private int routeStopId;
    private OrderStatus orderStatus;
    private Location location;

    public Order() {

    }

    //Standardmethode über REST
    public Order(DateTime _orderTime, String deliveryPlace, int weight) {
        if (_orderTime != null)
            this.orderTime = _orderTime;
        else
            this.orderTime = DateTime.now();
        this.location = new Location(deliveryPlace);
        this.weight = weight;

        Timer timer = new Timer();
        OrderTimer orderTimer = new OrderTimer(this, OrderStatus.Bereit);
        //Setze OrderStatus nach 5 Minuten auf Bereit
        timer.schedule(orderTimer, this.getOrderReadyTime().getMillis() - DateTime.now().getMillis());
        this.orderStatus = OrderStatus.InVorbereitung;
    }

    public Order(DateTime _orderTime, Location deliveryPlace, int weight) {
        this.orderTime = _orderTime;
        this.location = deliveryPlace;
        this.weight = weight;
    }

    public Order(int _orderId, DateTime _orderTime, Location _location, int _weight, int _orderStatus, int _routeStopId, DateTime _deliveryTime) {
        this.orderId = _orderId;
        this.orderTime = _orderTime;
        this.location = _location;
        this.weight = _weight;
        this.orderStatus = OrderStatus.values()[_orderStatus];
        this.routeStopId = _routeStopId;
        this.deliveryTime = _deliveryTime;
    }

    //TODO Validierung der Bestellungen: Zeitpunkt nicht vor 2017, Adresse irgendwie im Raum, Gewicht unter 4000
    public boolean validateOrder() {
        //TODO Locations dynamisch berechnen
        Depot depotLocation = DepotDAO.getDepot(1);
        DroneType droneType = DroneTypeDAO.getDroneType(1);

        if (weight >= 4000) {
            _log.error("Gewicht über 4000 g");
            return false;
        }
        double distance =  Location.distanceInKm(
            depotLocation.getLatitude(), depotLocation.getLongitude(),
            location.getLatitude(),location.getLongitude());

        if ( distance > (droneType.getMaxRange()/2)) {
            _log.error("Distanz zu groß! Vorhanden: {} Maximal: {}",distance, droneType.getMaxRange()/2);
            return false;
        }
        return true;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus != orderStatus) {
            this.orderStatus = orderStatus;
            OrderDAO.addOrderHistoryPoint(this);
        }
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

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
