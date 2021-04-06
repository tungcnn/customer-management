package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> listAllCustomers() {
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            return new ResponseEntity<List<Customer>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(value = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        customerService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        Customer currentCustomer = customerService.findById(id);
        if (currentCustomer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentCustomer.setFirstName(customer.getFirstName());
        currentCustomer.setLastName(customer.getLastName());
        currentCustomer.setId(customer.getId());

        customerService.save(currentCustomer);
        return new ResponseEntity<>(currentCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
        Customer customer = customerService.findById(id);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        customerService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
