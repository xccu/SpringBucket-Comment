package com.example.jedisdemo.repository;

import com.example.jedisdemo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
