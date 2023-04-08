package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.service.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/create-new-customer")
    public ResponseEntity<Customer> createNewCustomer(@Valid @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createNewCustomer(customer), HttpStatus.OK);
    }

    @GetMapping("/get-customer")
    public ResponseEntity<Customer> getCustomer(Long id) {
        return new ResponseEntity<>(customerService.getCustomer(id), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "Running successfully.";
    }

}
