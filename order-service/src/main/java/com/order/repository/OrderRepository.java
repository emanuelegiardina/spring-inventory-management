package com.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

    Optional<Order> findByIdAndCustomerId(Long id, String customerId);
    
}
