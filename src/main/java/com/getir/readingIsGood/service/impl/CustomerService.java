package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.repository.ICustomerRepository;
import com.getir.readingIsGood.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Transactional
    @Override
    public Optional<CustomerResponse> createNewCustomer(CustomerRequest customerRequest) {
        Customer newCustomer;

        try {
            boolean isEmailExist = customerRepository.existsByEmail(customerRequest.getEmail());

            if (isEmailExist) {
                log.warn("Email already exists. Email: {}", customerRequest.getEmail());
                return Optional.empty();
            }

            newCustomer = Customer.builder()
                    .name(customerRequest.getName())
                    .email(customerRequest.getEmail())
                    .birthDate(customerRequest.getBirthDate())
                    .address(customerRequest.getAddress())
                    .build();

            Customer result = customerRepository.save(newCustomer);

            log.info("New customer was created by username: {}. Customer info: {}", getUsername(), result);
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while creating new customer", exception);
        }

        return Optional.ofNullable(CustomerResponse.builder()
                .name(newCustomer.getName())
                .email(newCustomer.getEmail())
                .birthDate(newCustomer.getBirthDate())
                .address(newCustomer.getAddress())
                .build());
    }

    @Override
    public Optional<CustomerResponse> getCustomer(Long id) {
        Optional<Customer> customer;

        try {
            customer = customerRepository.findById(id);

            if (!customer.isPresent()) {
                log.warn("There is no customer with id: {}", id);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting customer.", exception);
        }

        return Optional.ofNullable(CustomerResponse.builder()
                .name(customer.get().getName())
                .email(customer.get().getEmail())
                .birthDate(customer.get().getBirthDate())
                .address(customer.get().getAddress())
                .build());
    }

    @Override
    public Optional<Customer> getCustomerWithId(Long id) {
        Optional<Customer> customer;

        try {
            customer = customerRepository.findById(id);

            if (!customer.isPresent()) {
                log.warn("There is no customer with id: {}", id);
                return Optional.empty();
            }
        } catch (Exception exception) {
            throw new ReadingIsGoodException("Error occurred while getting customer by id.", exception);
        }

        return customer;
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "";
    }

}
