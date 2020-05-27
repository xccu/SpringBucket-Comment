package com.example.jpademo.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.jpademo.model.CoffeeOrder;


public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, Long> {
}
