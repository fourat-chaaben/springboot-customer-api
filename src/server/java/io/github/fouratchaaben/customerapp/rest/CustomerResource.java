package io.github.fouratchaaben.customerapp.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.fouratchaaben.customerapp.model.Customer;
import io.github.fouratchaaben.customerapp.util.CustomerSortingOptions;
import io.github.fouratchaaben.customerapp.service.CustomerService;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("customers")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestParam(value = "sortField", defaultValue = "ID") CustomerSortingOptions.SortField sortField,
                                                          @RequestParam(value = "sortingOrder", defaultValue = "ASCENDING") CustomerSortingOptions.SortingOrder sortingOrder) {
        return ResponseEntity.ok(customerService.getAllCustomers(new CustomerSortingOptions(sortingOrder, sortField)));
    }


    @PostMapping("customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if (customer.getId() != null) {
            return ResponseEntity.badRequest().body(customer);
        }
        customerService.saveCustomer(customer);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("customers/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer, @PathVariable("customerId") UUID customerId) {
        if (!updatedCustomer.getId().equals(customerId)) {
            return ResponseEntity.badRequest().body(updatedCustomer);
        }
        customerService.saveCustomer(updatedCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

}
