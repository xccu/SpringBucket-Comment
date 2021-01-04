package com.example.demo.repository;

import com.example.demo.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 继承JpaRepository
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
