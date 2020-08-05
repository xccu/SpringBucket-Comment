package com.example.performanceaspectdemo.repository;

import com.example.performanceaspectdemo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
