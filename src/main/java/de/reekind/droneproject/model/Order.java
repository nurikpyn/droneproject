package de.reekind.droneproject.model;

import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Date;
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
    private int droneId;
    private Location location;
    private List<OrderHistoryPoint> orderHistoryPointList;
    private String TEEEST;


    //TODO Validierung der Bestellungen: Zeitpunkt nicht vor 2017, Adresse irgendwie im Raum, Gewicht unter 4000
    public Order() {
       /* Context ctx = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/droneprojectDB");

            con = ds.getConnection();
            stmt = con.createStatement();

            rs = stmt.executeQuery("select droneName from drones");

            if (rs.first()) {
                this.TEEEST = "First element found " + rs.getString("droneName");
            } else {
                this.TEEEST = "no rows found";
            }


        } catch (Exception ex) {
            this.TEEEST = "EXCEPTION";
        }*/


    }
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

    public String getTEEEST() {
        /*Context ctx = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/droneprojectDB");

            con = ds.getConnection();
            stmt = con.createStatement();

            rs = stmt.executeQuery("select droneName from drones");

            if (rs.first()) {
                this.TEEEST = "First element found " + rs.getString("droneName");
            } else {
                this.TEEEST = "no rows found";
            }


        } catch (Exception ex) {
            this.TEEEST = "EXCEPTION";
        }*/
        return TEEEST;
    }

    public void setTEEEST(String TEEEST) {
        this.TEEEST = TEEEST;
    }
}
