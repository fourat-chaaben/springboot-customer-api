package io.github.fouratchaaben.customerapp.service;

import static io.github.fouratchaaben.customerapp.util.CustomerSortingOptions.SortingOrder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.fouratchaaben.customerapp.model.Customer;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions;

@Service
public class CustomerService {
  	// do not change this
    private final List<Customer> customers;

    public CustomerService() {
        this.customers = new ArrayList<>();
    }

    public Customer saveCustomer(Customer customer) {
        var optionalCustomer = customers.stream().filter(existingCustomer -> existingCustomer.getId().equals(customer.getId())).findFirst();
        if (optionalCustomer.isEmpty()) {
            customer.setId(UUID.randomUUID());
            customers.add(customer);
            return customer;
        } else {
            var existingCustomer = optionalCustomer.get();
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setBirthday(customer.getBirthday());
            return existingCustomer;
        }
    }

    public void deleteCustomer(UUID customerId) {
        this.customers.removeIf(customer -> customer.getId().equals(customerId));
    }

    public List<Customer> getAllCustomers(CustomerSortingOptions sortingOptions) {
        if (sortingOptions == null) {
            return new ArrayList<>(this.customers);
        }
        var sortedList = new ArrayList<>(this.customers);
        sortedList.sort((p1, p2) -> {
            Customer customer1;
            Customer customer2;
            if (sortingOptions.getSortingOrder() == ASCENDING) {
                customer1 = p1;
                customer2 = p2;
            } else {
                customer1 = p2;
                customer2 = p1;
            }

            return switch (sortingOptions.getSortField()) {
                case ID -> customer1.getId().compareTo(customer2.getId());
                case FIRST_NAME -> customer1.getFirstName().compareTo(customer2.getFirstName());
                case LAST_NAME -> customer1.getLastName().compareTo(customer2.getLastName());
                case BIRTHDAY -> customer1.getBirthday().compareTo(customer2.getBirthday());
            };
        });
        return sortedList;
    }
}
