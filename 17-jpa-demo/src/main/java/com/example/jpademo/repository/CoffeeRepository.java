package com.example.jpademo.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.jpademo.model.Coffee;

public interface CoffeeRepository extends CrudRepository<Coffee, Long> {
}
