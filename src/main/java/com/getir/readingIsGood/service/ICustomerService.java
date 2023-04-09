package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.NewCustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import org.springframework.beans.InvalidPropertyException;

public interface ICustomerService {

    CustomerResponse createNewCustomer(NewCustomerRequest newCustomerRequest) throws InvalidPropertyException;

    CustomerResponse getCustomer(Long id);

}
