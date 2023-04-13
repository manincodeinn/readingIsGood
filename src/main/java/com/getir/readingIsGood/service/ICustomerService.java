package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import org.springframework.beans.InvalidPropertyException;

import java.util.Optional;

public interface ICustomerService {

    Optional<CustomerResponse> createNewCustomer(CustomerRequest customerRequest);

    Optional<CustomerResponse> getCustomer(Long id);

    Optional<Customer> getCustomerWithId(Long id);

}
