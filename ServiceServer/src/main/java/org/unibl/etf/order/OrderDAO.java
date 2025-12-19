package org.unibl.etf.order;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderDAO {

    private final Map<String, Order> orders;

    private static OrderDAO instance;

    private OrderDAO() {
        this.orders = new HashMap<>();
    }

    public static synchronized OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
        }
        return instance;
    }

    public void addOrder(Order order) {
        if (order != null && order.getId() != null) {
            orders.put(order.getId(), order);
        }
    }

    public Order getOrder(String id) {
        return orders.get(id);
    }

    public Collection<Order> getAllOrders() {
        return Collections.unmodifiableCollection(orders.values());
    }

    public void removeOrder(String id) {
        orders.remove(id);
    }

    public boolean containsOrder(String id) {
        return orders.containsKey(id);
    }
}
