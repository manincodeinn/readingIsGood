package com.getir.readingIsGood.service.impl;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.ReadingIsGoodException;
import com.getir.readingIsGood.repository.ICustomerRepository;
import com.getir.readingIsGood.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Transactional
    @Override
    public Customer createNewCustomer(Customer customer) throws InvalidPropertyException {
        boolean isEmailExist = customerRepository.existsByEmail(customer.getEmail());

        if(isEmailExist) {
            throw new ReadingIsGoodException("Email already exists.");
        }

        log.info("New customer was created: {}", customer.toString());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if(!customer.isPresent()) {
            StringBuilder sb = new StringBuilder();
            sb.append("There is no customer with id: ").append(id);
            throw new ReadingIsGoodException(sb.toString());
        }

        return customer.get();
    }

}
