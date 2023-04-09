package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.model.response.OrderResponse;
import com.getir.readingIsGood.repository.ICustomerRepository;
import com.getir.readingIsGood.service.ICustomerService;
import com.getir.readingIsGood.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Transactional
    @Override
    public CustomerResponse createNewCustomer(CustomerRequest customerRequest) throws InvalidPropertyException {
        boolean isEmailExist = customerRepository.existsByEmail(customerRequest.getEmail());

        if (isEmailExist) {
            throw new ReadingIsGoodException("Email already exists.");
        }

        Customer newCustomer = Customer.builder()
                .name(customerRequest.getName())
                .email(customerRequest.getEmail())
                .birthDate(customerRequest.getBirthDate())
                .address(customerRequest.getAddress())
                .build();

        customerRepository.save(newCustomer);

        log.info("New customer was created. {}", newCustomer);

        return CustomerResponse.builder()
                .name(newCustomer.getName())
                .email(newCustomer.getEmail())
                .birthDate(newCustomer.getBirthDate())
                .address(newCustomer.getAddress())
                .build();
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no customer with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return CustomerResponse.builder()
                .name(customer.get().getName())
                .email(customer.get().getEmail())
                .birthDate(customer.get().getBirthDate())
                .address(customer.get().getAddress())
                .build();
    }

    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            log.info("There is no customer with id: {}", id);
            throw new ReadingIsGoodException("There is no customer.");
        }

        return customer.get();
    }

}
