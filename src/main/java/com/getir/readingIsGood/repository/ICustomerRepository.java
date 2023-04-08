package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}
