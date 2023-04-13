package com.getir.readingIsGood.service;

import com.getir.readingIsGood.entity.Customer;
import com.getir.readingIsGood.model.request.CustomerRequest;
import com.getir.readingIsGood.model.response.CustomerResponse;
import com.getir.readingIsGood.repository.ICustomerRepository;
import com.getir.readingIsGood.service.impl.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private ICustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    private static final CustomerRequest customerRequest = CustomerRequest.builder()
            .name("getir")
            .email("getir@getir.com")
            .birthDate(LocalDate.of(2015, 7, 1))
            .address("Getir bi mutluluk")
            .build();

    private static final Customer customer = Customer.builder()
            .id(12345L)
            .name(customerRequest.getName())
            .email(customerRequest.getEmail())
            .birthDate(customerRequest.getBirthDate())
            .address(customerRequest.getAddress())
            .build();

    @Test
    void createNewCustomerTest() {
        when(customerRepository.existsByEmail(Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Optional<CustomerResponse> newCustomer = customerService.createNewCustomer(customerRequest);

        assertNotEquals(Optional.empty(), newCustomer);
        assertEquals(customerRequest.getName(), newCustomer.get().getName());
        assertEquals(customerRequest.getEmail(), newCustomer.get().getEmail());
        assertEquals(customerRequest.getBirthDate(), newCustomer.get().getBirthDate());
        assertEquals(customerRequest.getAddress(), newCustomer.get().getAddress());

        assertEquals(Optional.empty(), customerService.createNewCustomer(customerRequest));
    }

    @Test
    void getCustomerTest() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.ofNullable(customer));

        Optional<CustomerResponse> customer1 = customerService.getCustomer(customer.getId());

        assertNotEquals(Optional.empty(), customer1);
        assertEquals(customer.getName(), customer1.get().getName());
        assertEquals(customer.getEmail(), customer1.get().getEmail());
        assertEquals(customer.getBirthDate(), customer1.get().getBirthDate());
        assertEquals(customer.getAddress(), customer1.get().getAddress());

        assertEquals(Optional.empty(), customerService.getCustomer(0L));
    }

    @Test
    void getCustomerWithIdTest() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.ofNullable(customer));

        Optional<Customer> customerWithId = customerService.getCustomerWithId(customer.getId());

        assertNotEquals(Optional.empty(), customerWithId);
        assertEquals(customer.getName(), customerWithId.get().getName());
        assertEquals(customer.getEmail(), customerWithId.get().getEmail());
        assertEquals(customer.getBirthDate(), customerWithId.get().getBirthDate());
        assertEquals(customer.getAddress(), customerWithId.get().getAddress());

        assertEquals(Optional.empty(), customerService.getCustomerWithId(0L));
    }

}
