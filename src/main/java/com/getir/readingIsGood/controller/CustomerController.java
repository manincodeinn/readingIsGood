package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/create-new-customer")
    public ResponseEntity<CustomerResponse> createNewCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Create new customer request was received.");

        Optional<CustomerResponse> newCustomer = customerService.createNewCustomer(customerRequest);

        return newCustomer.map(customerResponse -> new ResponseEntity<>(customerResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/get-customer")
    public ResponseEntity<CustomerResponse> getCustomer(@RequestParam Long id) {
        Optional<CustomerResponse> customer = customerService.getCustomer(id);

        return customer.map(customerResponse -> new ResponseEntity<>(customerResponse, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/test")
    public String test() {
        return "Running successfully.";
    }

}
