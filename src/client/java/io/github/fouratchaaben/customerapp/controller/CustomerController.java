package io.github.fouratchaaben.customerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.fouratchaaben.customerapp.model.Customer;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions;

public class CustomerController {
    private final WebClient webClient;
    private final List<Customer> customers;

    public CustomerController() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.customers = new ArrayList<>();
    }

    public void getAllCustomers(CustomerSortingOptions sortingOptions, Consumer<List<Customer>> customerConsumer) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("customers")
                        .queryParam("sortField", sortingOptions.getSortField())
                        .queryParam("sortingOrder", sortingOptions.getSortingOrder())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Customer>>() {
                })
                .onErrorStop()
                .subscribe(newCustomers -> {
                    customers.clear();
                    customers.addAll(newCustomers);
                    customerConsumer.accept(customers);
                });
    }

    public void addCustomer(Customer customer, Consumer<List<Customer>> customerConsumer) {
        webClient.post()
                .uri("customers")
                .bodyValue(customer)
                .retrieve()
                .bodyToMono(Customer.class)
                .onErrorStop()
                .subscribe(newCustomer -> {
                    customers.add(newCustomer);
                    customerConsumer.accept(customers);
                });

    }

    public void updateCustomer(Customer customer, Consumer<List<Customer>> customerConsumer) {
        webClient.put()
                .uri("customers/" + customer.getId())
                .bodyValue(customer)
                .retrieve()
                .bodyToMono(Customer.class)
                .onErrorStop()
                .subscribe(updatedCustomer -> {
                    customers.stream()
                                    .filter(c->c.getId().equals(customer.getId()))
                                            .findFirst()
                                                    .ifPresent(customer1 ->
                                                    {
                                                     customer1.setFirstName(updatedCustomer.getFirstName());
                                                     customer1.setLastName(updatedCustomer.getLastName());
                                                     customer1.setBirthday(updatedCustomer.getBirthday());
                                                    });
                    customerConsumer.accept(customers);
                });

    }

    public void deleteCustomer(Customer customer, Consumer<List<Customer>> customerConsumer) {
        webClient.delete()
                .uri("customers/" + customer.getId())
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorStop()
                .doOnSuccess(voidResponse -> {
                    customers.removeIf(c -> c.getId().equals(customer.getId()));
                    customerConsumer.accept(customers);
                })
                .block();

    }

}
