package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.model.response.OrderResponse;
import org.springframework.beans.InvalidPropertyException;

import java.util.List;

public interface ICustomerService {

    CustomerResponse createNewCustomer(CustomerRequest customerRequest) throws InvalidPropertyException;

    CustomerResponse getCustomer(Long id);

    Customer getCustomerById(Long id);

}
