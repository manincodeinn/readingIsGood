package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
