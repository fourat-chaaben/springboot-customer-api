package io.github.fouratchaaben.customerapp.controller;

import io.github.fouratchaaben.customerapp.model.Order;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class OrderController {

    private static final String BASE_URL = "http://localhost:8080/orders";

    private final WebClient webClient;
    private final List<Order> orders;

    public OrderController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order.OrderRequest request, Consumer<List<Order>> consumer) {
        webClient.post()
                .uri("/orders")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Order.class)
                .onErrorStop()
                .subscribe(savedOrder -> {
                    orders.add(savedOrder);
                    consumer.accept(orders);
                });
    }

    public void updateOrder(UUID orderId, Order.OrderRequest request, Consumer<List<Order>> consumer) {
        webClient.put()
                .uri("/orders/{id}", orderId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Order.class)
                .onErrorStop()
                .subscribe(updatedOrder -> {
                    orders.stream()
                            .filter(order -> order.getId().equals(orderId))
                            .findFirst()
                            .ifPresent(order -> {
                                order.setCustomerId(updatedOrder.getCustomerId());
                                order.setProduct(updatedOrder.getProduct());
                                order.setOrderedOn(updatedOrder.getOrderedOn());
                            });
                    consumer.accept(orders);
                });
    }

    public void deleteOrder(UUID orderId, Consumer<List<Order>> consumer) {
        webClient.delete()
                .uri("/orders/{id}", orderId)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorStop()
                .doOnSuccess(ignored -> {
                    orders.removeIf(order -> order.getId().equals(orderId));
                    consumer.accept(orders);
                })
                .block();
    }

    public void getAllOrders(Long from, Long to, Consumer<List<Order>> consumer) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/orders")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Order>>() {
                })
                .onErrorStop()
                .subscribe(fetchedOrders -> {
                    orders.clear();
                    orders.addAll(fetchedOrders);
                    consumer.accept(orders);
                });
    }
}
