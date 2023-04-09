package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.NewCustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/create-new-customer")
    public ResponseEntity<CustomerResponse> createNewCustomer(@Valid @RequestBody NewCustomerRequest newCustomerRequest) {
        log.info("New customer request was received.");
        return new ResponseEntity<>(customerService.createNewCustomer(newCustomerRequest), HttpStatus.OK);
    }

    @GetMapping("/get-customer")
    public ResponseEntity<CustomerResponse> getCustomer(Long id) {
        return new ResponseEntity<>(customerService.getCustomer(id), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "Running successfully.";
    }

}
