package io.github.fouratchaaben.customerapp.rest;

import io.github.fouratchaaben.customerapp.model.Customer;
import io.github.fouratchaaben.customerapp.model.Order;
import io.github.fouratchaaben.customerapp.model.Order.ProductType;
import io.github.fouratchaaben.customerapp.service.CustomerService;
import io.github.fouratchaaben.customerapp.service.OrderService;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/orders",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class OrderResource {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderResource(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) Long from,
            @RequestParam(required = false) Long to) {
        return ResponseEntity.ok(orderService.getAllOrders(to, from));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order.OrderRequest request) {
        Set<UUID> customerIds = customerService.getAllCustomers(new CustomerSortingOptions())
                .stream()
                .map(Customer::getId)
                .collect(Collectors.toSet());

        if (!customerIds.contains(request.getCustomerId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ProductType.valueOf(request.getProduct());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProduct(ProductType.valueOf(request.getProduct()));
        order.setOrderedOn(LocalDate.now());

        return ResponseEntity.ok(orderService.saveOrder(order));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order.OrderRequest request,
                                             @PathVariable UUID orderId) {
        boolean customerExists = customerService.getAllCustomers(new CustomerSortingOptions())
                .stream()
                .anyMatch(c -> c.getId().equals(request.getCustomerId()));

        boolean orderExists = orderService.getAllOrders(null, null)
                .stream()
                .anyMatch(o -> o.getId().equals(orderId));

        if (!customerExists || !orderExists) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ProductType.valueOf(request.getProduct());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        Order order = new Order();
        order.setId(orderId);
        order.setCustomerId(request.getCustomerId());
        order.setProduct(ProductType.valueOf(request.getProduct()));
        order.setOrderedOn(LocalDate.now());

        return ResponseEntity.ok(orderService.saveOrder(order));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
