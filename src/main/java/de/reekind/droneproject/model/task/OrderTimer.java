package de.reekind.droneproject.model.task;

import de.reekind.droneproject.dao.OrderDAO;
import de.reekind.droneproject.model.Order;
import de.reekind.droneproject.model.enumeration.OrderStatus;

import java.util.TimerTask;

public class OrderTimer extends TimerTask {
    private Order order;
    private OrderStatus orderStatus;

    public OrderTimer(Order order, OrderStatus statusToSet) {
        this.order = order;
        this.orderStatus = statusToSet;
    }

    @Override
    public void run() {
        this.order.setOrderStatus(orderStatus);
        OrderDAO.updateOrder(order);
    }
}
