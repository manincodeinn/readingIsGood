package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import org.springframework.beans.InvalidPropertyException;

public interface ICustomerService {

    Customer createNewCustomer(Customer customer) throws InvalidPropertyException;

    Customer getCustomer(Long id);

}
