package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
}
