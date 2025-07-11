package io.github.fouratchaaben.customerapp.service;


import io.github.fouratchaaben.customerapp.model.Order;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    // do not change this
    private final List<Order> orders;

    public OrderService() {
        this.orders = new ArrayList<>();
    }

    public Order saveOrder(Order order) {
        var optionalOrder = orders.stream().filter(existingOrder -> existingOrder.getId().equals(order.getId())).findFirst();
        if (optionalOrder.isEmpty()) {
            order.setId(UUID.randomUUID());
            orders.add(order);
            return order;
        }
        var existingOrder = optionalOrder.get();
        existingOrder.setCustomerId(order.getCustomerId());
        existingOrder.setProduct(order.getProduct());
        existingOrder.setOrderedOn(order.getOrderedOn());
        return existingOrder;
    }

    public void deleteOrder(UUID orderId) {
        this.orders.removeIf(order -> order.getId().equals(orderId));
    }

    public List<Order> getAllOrders(Long to, Long from) {
        if (from == null) {
            from = 0L;
        }
        if (to == null) {
            to = System.currentTimeMillis();
        }
        Instant instantFrom = Instant.ofEpochMilli(from);
        LocalDate fromDate = instantFrom.atZone(ZoneId.systemDefault()).toLocalDate();
        Instant instantTo = Instant.ofEpochMilli(to);
        LocalDate toDate = instantTo.atZone(ZoneId.systemDefault()).toLocalDate();
        List<Order> sortedOrders = this.orders.stream().filter(order -> {
            if (order.getOrderedOn().compareTo(fromDate) < 0) {
                return false;
            }
            if (order.getOrderedOn().compareTo(toDate) > 0) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        sortedOrders.sort((o1, o2) -> o1.getOrderedOn().compareTo(o2.getOrderedOn()));
        return sortedOrders;
    }
}